package kangaroo.api

import grails.converters.JSON
import kangaroo.Course
import kangaroo.Term

class ApiCourseController extends ApiBaseController {

    def list = {
        def map = Term.list().collectEntries { term -> [term.id, Course.findAllByTerm(term).collectEntries {[it.id, it]}] }
        render(map as JSON)
    }

    def show = {
        def course = getSelected()
        if (course)
            render(course as JSON)
        else
            notFoundError()
    }

    Course getSelected() { Course.get(params.id) }

    def notFoundError() {
        response.status = 404;
        render([error: "NotFound", errorMessage: "The indicated course could not be found."] as JSON)
    }
}
