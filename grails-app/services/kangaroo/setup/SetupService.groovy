package kangaroo.setup

import grails.converters.JSON
import kangaroo.AppUtils
import kangaroo.Building
import kangaroo.Professor
import kangaroo.Term
import kangaroo.mn.ProfessorOfficeHours
import kangaroo.mn.Teaching

class SetupService {

    public static SetupService instance;

    private def outbackRoot;

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
            importPeople()

            setStatus("succeeded", "All done!")
        }
        catch (Exception e) {
            e.printStackTrace()
            setStageStatus("failed", "Exception: " + e.toString())
            setStatus("failed", "Shoot! Error while importing ${currentStage().name}...")
        }
    }

    def clearData() {
        startStage("Clearing Data")
        clearTable(Building)
        clearTable(Teaching)
        clearTable(ProfessorOfficeHours)
        clearTable(Professor)
        clearTable(Term)
        setStageStatus("succeeded", "All existing data cleared.")
    }

    def importTerms() {
        startStage("Terms")
        fetchJson("/term").values().each { term ->
            AppUtils.ensureNoErrors(Term.fromJsonObject(term));
        }
        setStageStatus("succeeded", "${Term.count()} terms.");
    }

    def importPeople() {
        startStage("People")
        def json = fetchJson("/person");

        json.faculty.values().each { savePersonFromJson(it, true) }
        json.staff.values().each { savePersonFromJson(it, false) }

        setStageStatus("succeeded", "${Professor.count()} people.")
    }

    def importBuildings() {
        startStage("Buildings")
        fetchJson("/building").values().each { building ->
            AppUtils.ensureNoErrors(Building.fromJsonObject(building))
        }
        setStageStatus("succeeded", Building.count + " buidings.")
    }

    /**
     * Helper method for importPeople().
     */
    def savePersonFromJson(def object, boolean isProfessor) {
        logStage("Saving ${object.firstName} ${object.lastName} (" + (isProfessor ? "Professor" : "Staff Member'") + ")")
        def person = Professor.fromJsonObject(object)
        person.isProfessor = isProfessor;
        AppUtils.ensureNoErrors(person.save())
    }

    /**
     * Deletes all objects of the given domain class. Throws an exception if unsuccessful.
     *
     * @example clearTable (Course)
     * @todo Use a TRUNCATE call instead (faster)
     */
    def clearTable(type) {
        logStage("Clearing ${type}...");
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
