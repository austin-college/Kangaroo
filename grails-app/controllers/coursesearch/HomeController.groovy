package coursesearch

class HomeController {

    def facultyFetcherService
    def courseListParseService

    def index = {
        def result = courseListParseService.parseScrape()
        println result
        [result:result]
    }
}
