package coursesearch

class HomeController {

    def searchService

    def index = {
       [tableJson: searchService.coursesTableCached]
    }
}
