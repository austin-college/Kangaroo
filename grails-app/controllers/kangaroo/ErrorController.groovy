package kangaroo

import grails.converters.JSON
import grails.util.Environment

/**
 * [PC] Handles any error the application experiences.
 *
 * Some notes:
 * - productionError.gsp is used in production.
 *
 * - developmentError.gsp is called instead of error.gsp in development environments. It doesn't report the bugs automatically and
 *      makes it easier to read the error details.
 *
 * - If the error handler itself fails (just serverError, not any of the reporting code), you simply get "Internal server error".
 *
 * - You can test the handler simply by going to /error.
 */
class ErrorController {

    // Sends bugs to the bug tracker.
    def bugReportService;

    def index = {
        throw new IOException("This is the test exception!");
    }

    def serverError = {

        session.bugTitle = "";
        session.bugStackTrace = "";

        // Prepare the bug report while we still have access to the exception.
        if (request.exception != null) {

            // Create the title.
            session.bugTitle = "(${request.exception.className}:${request.exception.lineNumber}) ${request.exception.message}".split("\n")[0];

            // Create the stack trace.
            request.exception.stackTraceLines.each { line ->
                session.bugStackTrace += line + "\n";
            }
        }

        if (Environment.current == Environment.PRODUCTION)
            render(view: "productionError", model: [bugTitle: session.bugTitle]);
        else
            render(view: "developmentError", model: [bugTitle: session.bugTitle]);
    }

    /**
     * Invoked by ajax as soon as the error page loads.
     */
    def reportBug = {

        def bugDetails = "${activeProfessorString} encountered an error:\n\n"

        bugDetails += "[code]${session.bugStackTrace}[/code]\n\n";

        if (params.reportDetails)
            bugDetails += "Details: ${params.reportDetails}\n";

        if (Environment.current == Environment.PRODUCTION)
            bugDetails += "Environment: Production\n";
        else
            bugDetails += "Environment: Development\n";

        bugDetails += "Browser: " + params.browser + "\n";
        bugDetails += "Opened from: ${params.sourceUri}";

        def response = bugReportService.reportBug(session.bugTitle, bugDetails, activeProfessor?.email);
        println response
        render(response as JSON);
    }

    /**
     * Invoked by ajax whenever the user submits extra details.
     */
    def addBugDetails = {

        def bugDetails;

        bugDetails = "${activeProfessorString} has returned to add some details:\n\n"
        bugDetails += params.reportDetails + "\n";

        render(bugReportService.reportBug(session.bugTitle, bugDetails, activeProfessor?.email) as JSON);
    }

    /**
     * Returns the current "logged in" professor (meaning they set office hours recently and are still in session) or null.
     */
    Professor getActiveProfessor() {
        if (session.professorId)
            Professor.get(session.professorId);
    }

    String getActiveProfessorString() {
        if (activeProfessor)
            "${activeProfessor} (${activeProfessor.id})"
        else
            "A guest"
    }
}