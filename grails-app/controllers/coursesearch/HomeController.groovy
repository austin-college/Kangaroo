package coursesearch

import grails.converters.JSON
import coursesearch.data.BackendDataService

class HomeController {

    def dataTablesService

    def index = {
        [tableJson: dataTablesService.getTableCached(BackendDataService.currentTerm), departmentsJson: (getDepartmentsMap() as JSON)]
    }

    /**
     * Called via AJAX: returns the table data for the given term in JSON form.
     */
    def getData = {
        render([table: JSON.parse(dataTablesService.getTableCached(Term.findByShortCode(params.term)))] as JSON)
    }

    def getDepartmentsMap() {
        def map = [:]
        Department.list().each { map[it.code] = it.name }
        return map;
    }
}
