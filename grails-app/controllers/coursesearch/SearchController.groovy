package coursesearch

import grails.converters.JSON

class SearchController {

    def searchService

    def index = { }

    def getClassesForTable = {
        render(searchService.getCoursesForTable(params) as JSON);
    }
}
