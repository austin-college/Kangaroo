package kangaroo.setup

import grails.converters.JSON
import kangaroo.Term

class SetupService {

    public static SetupService instance;

    private def outbackRoot;

    public def status = [:];

    def runSetup(String outbackRoot = "http://kangaroo.austincollege.edu/api") {
        instance = this;
        this.outbackRoot = outbackRoot;

        log("Starting import...")
        try {
            importTerms()

            log("All done!", "done")
        }
        catch (Exception e) {
            log(e.toString(), "error")
        }
    }

    def importTerms() {
        log("Importing terms...")

        clearTable(Term);
        fetchJson("/term").values().each { term ->
            Term.fromJsonObject(term).save();
        }

        log(Term.count() + " terms.");
    }

    /**
     * Deletes all objects of the given domain class. Throws an exception if unsuccessful.
     *
     * @example clearTable ( Course )
     * @todo Use a TRUNCATE call instead (faster)
     */
    def clearTable(type) {
        type.findAll().each { it.delete(flush: true) }

        if (type.count() > 0)
            throw new Exception("The table related to $type was not emptied successfully.")
    }

    def log(String message, String status = "running") {
        this.status = [status: status, text: message]
        println "[Setup " + status + ": " + message + "]"
    }

    def fetchJson(path) {
        return JSON.parse(new URL(outbackRoot + path).text);
    }


}
