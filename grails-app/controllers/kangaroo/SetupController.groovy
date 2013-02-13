package kangaroo

import grails.converters.JSON
import kangaroo.setup.SetupService
import grails.util.Environment

class SetupController {

    def setupService

    def beforeInterceptor = {
        // Be careful; setup is a security risk.
        if (!setupAllowed()) {
            redirect(controller: "home")
            return false;
        }
    }

    def index = {}

    def startImport = {
        setupService.runSetup();
        render([] as JSON)
    }

    def getStatus = {
        if (SetupService.instance)
            render(SetupService.instance.status as JSON)
        else
            render([status: "unknown", message: "Waiting for import to begin...", stages: []] as JSON)
    }

    /**
     * Returns if setup must be run before Kangaroo can function.
     */
    static boolean setupRequired() {
        return (Term.count() == 0 || Course.count() == 0 || Professor.count() == 0);
    }

    /**
     * Returns if the current user is allowed to run setup.
     *
     * @todo Also allow admins to run setup whenever they want.
     */
    static boolean setupAllowed() {
        return setupRequired() || (Environment.current == Environment.DEVELOPMENT); // Always allow fresh setup in dev.
    }
}
