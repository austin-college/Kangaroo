package kangaroo.api

import grails.converters.JSON
import kangaroo.Course
import kangaroo.Term

class ApiTermController {

    def list = {
        render(Term.list().collectEntries { [it.id, it] }.sort() as JSON)
    }

    def show = {
        def term = getSelected()
        if (term) {

            def map = [id: term.id, description: term.fullDescription, year: term.year, season: term.season, isActive: term.id == Term.CURRENT_TERM_CODE, courses: Course.findAllByTerm(term).collectEntries {[it.id, it] }];
            render(map as JSON)
        }
        else
            notFoundError()
    }


    Term getSelected() { Term.get(params.id) }

    def notFoundError() {
        response.status = 404;
        render([error: "NotFound", errorMessage: "The indicated term could not be found."] as JSON)
    }

}
