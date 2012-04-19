package kangaroo.api

import grails.converters.JSON
import kangaroo.Term

class ApiTermController {

    def list = {

        render(Term.list().collectEntries { [it.id, it] }.sort() as JSON)
    }

    def show = {
        def term = getSelected()
        if (term)
            render(term as JSON)
        else
            notFoundError()
    }


    Term getSelected() { Term.get(params.id) }

    def notFoundError() {
        response.status = 404;
        render([error: "NotFound", errorMessage: "The indicated term could not be found."] as JSON)
    }

}
