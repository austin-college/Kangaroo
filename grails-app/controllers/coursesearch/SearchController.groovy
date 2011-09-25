package coursesearch

class SearchController {

    def searchService

    def index = { }

    def getClassesForTable = {

        def start = System.currentTimeMillis()

        String results = searchService.coursesTableCached
        render(contentType: "application/json", text: results);
        println "done in ${(System.currentTimeMillis() - start) / 1000.0} seconds"
    }
}
