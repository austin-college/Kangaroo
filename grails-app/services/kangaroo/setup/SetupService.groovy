package kangaroo.setup

import grails.converters.JSON
import kangaroo.Professor
import kangaroo.Term

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
            importTerms()
            importPeople()

            setStatus("succeeded", "All done!")
        }
        catch (Exception e) {
            setStageStatus("failed", "Exception: " + e.message)
            setStatus("failed", "Shoot! We hit a bump while importing ${currentStage().name}...")
        }
    }

    def importTerms() {
        startStage("Terms")
        clearTable(Term);
        fetchJson("/term").values().each { term ->
            Term.fromJsonObject(term).save();
        }
        setStageStatus("succeeded", "${Term.count()} terms.");
    }

    def importPeople() {
        startStage("People")
        clearTable(Professor)

        setStageStatus("succeeded", "${Professor.count()} people.")
    }

    /**
     * Deletes all objects of the given domain class. Throws an exception if unsuccessful.
     *
     * @example clearTable ( Course )
     * @todo Use a TRUNCATE call instead (faster)
     */
    def clearTable(type) {
        logStage("Clearing $type...");
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
