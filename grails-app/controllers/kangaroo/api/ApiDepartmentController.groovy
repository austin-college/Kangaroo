package kangaroo.api

import grails.converters.JSON
import kangaroo.Department

class ApiDepartmentController {

    def list = {
        render(Department.list() as JSON)
    }
}
