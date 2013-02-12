package kangaroo

import grails.converters.JSON

class HomeController {

    def dataTablesService

    def index = {
        if (SetupController.setupRequired()) {
            redirect(controller: "setup")
            return;
        }

        []
    }

    /**
     * Called via AJAX: returns the table data for the given term in JSON form.
     */
    def getData = {
        render([table: dataTablesService.generateTableData(Term.get(params.term))] as JSON)
    }

    def robots = {
        render(view: "/robots", contentType: "text/plain")
    }
}
