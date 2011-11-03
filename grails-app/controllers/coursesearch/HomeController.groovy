package coursesearch

import grails.converters.JSON

class HomeController {

    def dataTablesService

    def index = {}

    def getData = {
        render([tableHtml: g.render(template: 'emptyTable'),
                table: JSON.parse(dataTablesService.getTableCached(Term.findByShortCode(params.term)))] as JSON)
    }
}
