package coursesearch

import grails.converters.JSON

class SearchController {

    def searchService

    def index = { }

    def getClassesForTable = {
        def start = System.currentTimeMillis()
        render(searchService.getCoursesForTable(params) as JSON);
        println "done in ${(System.currentTimeMillis() - start)/1000.0} seconds"
    }
}
