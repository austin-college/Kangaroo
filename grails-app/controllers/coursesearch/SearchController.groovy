package coursesearch

import grails.converters.JSON

class SearchController {

    def index = { }

    def getClassesAjax = {
        if (params.s) {
            def classes = Course.withCriteria {
                ilike("name", "%${params.s}%")
            }
            println classes.size()
            render(classes as JSON);
        }
    }
}
