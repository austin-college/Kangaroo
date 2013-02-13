package kangaroo.setup

import grails.converters.JSON
import kangaroo.*
import kangaroo.mn.CourseFulfillsRequirement
import kangaroo.mn.CourseMeetingTime
import kangaroo.mn.ProfessorOfficeHours
import kangaroo.mn.Teaching

class SetupService {

    public static SetupService instance;

    private def outbackRoot;
    def cacheService

    public def status = [:]

    def runSetup(String outbackRoot = "http://kangaroo.austincollege.edu/api") {
        instance = this;
        this.outbackRoot = outbackRoot;

        reset();
        setStatus("running", "Running import...")
        try {
            clearData()
            importTerms()
            importBuildings()
            importPhoneNumbers()
            importRooRouteStop()
            importRequirements()
            importDepartments()
            importMajors()
            importPeople()
            importCourses()
            initCache()
            setStatus("succeeded", "All done!")
        }
        catch (Exception e) {
            e.printStackTrace()
            setStageStatus("failed", "Exception: " + e.toString())
            setStatus("failed", "Shoot! Error while importing ${currentStage().name}...")
        }
    }

    /**
     * Clears all data from all tables that will be touched by the importer.
     *
     * Note that there are some dependencies (foreign key references) between tables, so
     * some tables must be cleared before others. I've grouped these tables with blank lines.
     *
     * Here are the explicit dependencies:
     * - Department is referred to by Major, Course (clear Major and Course first)
     * - Professor is referred to by Teaching, ProfessorOfficeHours (clear Teaching, ProfessorOfficeHours first...)
     * - Term is referred to by Teaching
     *
     * http://xkcd.com/754/
     */
    def clearData() {
        startStage("Clearing Existing Data")

        cacheService.clearCache()

        // Remove m-n classes first.
        clearTable(CourseFulfillsRequirement)
        clearTable(CourseMeetingTime)
        clearTable(ProfessorOfficeHours)
        clearTable(Teaching)

        clearTable(Requirement)
        clearTable(Major)
        clearTable(Professor)

        clearTable(PhoneNumber)
        clearTable(RooRouteStop)
        clearTable(Building)
        clearTable(Course)

        clearTable(Department)
        clearTable(Term)
        setStageStatus("succeeded", "All existing data cleared.")
    }

    def importTerms() {
        startStage("Terms")
        fetchJson("/term").values().each { Term.saveFromJsonObject(it) }
        setStageStatus("succeeded", "${Term.count()} terms.");
    }

    def importPeople() {
        startStage("People")
        def json = fetchJson("/person");

        json.faculty.values().each { savePersonFromJson(it, true) }
        json.staff.values().each { savePersonFromJson(it, false) }

        setStageStatus("succeeded", Professor.count() + " people (" + Professor.countByIsProfessor(true) + " faculty and " + Professor.countByIsProfessor(false) + " staff)")
    }

    def importBuildings() {
        startStage("Buildings")
        fetchJson("/building").values().each { Building.saveFromJsonObject(it) }
        setStageStatus("succeeded", Building.count + " buildings.")
    }

    def importDepartments() {
        startStage("Departments")
        fetchJson("/department").each { Department.saveFromJsonObject(it) }
        setStageStatus("succeeded", Department.count + " departments.")
    }

    def importMajors() {
        startStage("Majors")
        fetchJson("/major").each { Major.saveFromJsonObject(it) }
        setStageStatus("succeeded", Major.count + " majors.")
    }

    def importRequirements() {
        startStage("Requirements")
        fetchJson("/requirement").each { Requirement.saveFromJsonObject(it) }
        setStageStatus("succeeded", Requirement.count + " requirements.")
    }

    def importRooRouteStop() {
        startStage("Roo Route")
        fetchJson("/rooRoute").values().each { RooRouteStop.saveFromJsonObject(it) }
        setStageStatus("succeeded", RooRouteStop.count + " stops.")
    }

    def importCourses() {
        Term.list().each { Term term ->
            startStage("Courses (${term.id})")
            fetchJson("/term/" + term.id).courses.values().each {
                logStage("Saving ${it.name}...")
                Course.saveFromJsonObject(it, term)
            }
            setStageStatus("succeeded", "Some courses in $term.")
        }
    }

    def importPhoneNumbers() {
        startStage("Phone Numbers")
        fetchJson("/importantNumbers").each { PhoneNumber.saveFromJsonObject(it) }
        setStageStatus("succeeded", PhoneNumber.count + " numbers.")
    }

    def initCache() {
        startStage("Init Cache")
        cacheService.initializeCacheIfNeeded()
        setStageStatus("succeeded", "Cache is ready to go")
    }

    /**
     * Helper method for importPeople().
     */
    def savePersonFromJson(def object, boolean isProfessor) {
        logStage("Saving ${object.firstName} ${object.lastName} (" + (isProfessor ? "Professor" : "Staff Member") + ")")
        def person = Professor.saveFromJsonObject(object, isProfessor)
        AppUtils.ensureNoErrors(person.save())
    }

    /**
     * Deletes all objects of the given domain class. Throws an exception if unsuccessful.
     *
     * @example clearTable (Course)
     * @todo Use a TRUNCATE call instead (faster)
     */
    def clearTable(type) {
        logStage("Clearing data for ${type}...");
        type.findAll().each { it.delete(flush: true) }

        if (type.count() > 0)
            throw new Exception("The table related to $type was not emptied successfully.")
    }

    def reset() {
        status = [
                status: "Unknown",
                message: "The state of the import is unknown.",
                stages: []
        ];

        println "\n** Restarting setup **\n"
    }

    /**
     * Logs about the global import process.
     */
    def log(String message) {
        this.status.message = message
        println "[Setup: $message]"
    }

    /**
     * Logs about the current stage only.
     */
    def logStage(String message) {
        currentStage().message = message;
        println "[Setup ${currentStage().name}: $message]"
    }

    def setStatus(String status, String message) {
        this.status.status = status;
        this.status.message = message;
        println "[Setup is now '$status': $message]"
    }

    def setStageStatus(String status, String message) {
        currentStage().status = status;
        currentStage().message = message;
        println "[Setup ${currentStage().name} is now '$status': $message]"
    }

    def currentStage() { status.stages.last() }

    def startStage(String name) {
        status.stages << [name: name, status: "running", message: "$name started"];
    }

    def fetchJson(path) {
        return JSON.parse(new URL(outbackRoot + path).text);
    }
}
