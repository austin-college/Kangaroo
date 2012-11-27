package kangaroo

import grails.converters.JSON
import kangaroo.setup.SetupService

class SetupController {

    def setupService

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
}
