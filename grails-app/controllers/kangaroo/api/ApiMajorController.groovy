package kangaroo.api

import grails.converters.JSON
import kangaroo.Major

class ApiMajorController {

    def list = {
        render(Major.list() as JSON)
    }
}
