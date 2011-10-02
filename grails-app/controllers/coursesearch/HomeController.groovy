package coursesearch

class HomeController {

    def dataTablesService

    def index = {
       [tableJson: dataTablesService.getTableCached()]
    }
}
