package kangaroo.api

import grails.converters.JSON
import kangaroo.Requirement

class ApiRequirementController {

    def list = {
        render(Requirement.list() as JSON)
    }
}
