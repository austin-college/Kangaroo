package kangaroo.api

import grails.converters.JSON

class ApiHomeController {

    def index = {
        render([ApiVersion: 100,
                MinApiVersion: 100,  // The mininum API versions clients must use to understand our responses.
                ServerVersion: grailsApplication.metadata.'app.version'] as JSON);
    }
}
