package coursesearch

class AdminController {

    def courseListParseService

    def index = { }

    def reparse = {
        flash.message = courseListParseService.parseScrape();
        redirect(action: 'index')
    }
}
