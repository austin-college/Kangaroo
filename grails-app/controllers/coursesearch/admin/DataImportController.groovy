package coursesearch.admin

import coursesearch.Course
import coursesearch.Term

class DataImportController {

    def courseImporterService

    def index = {}

    def submit = {
        def term = Term.findByShortCode(params.term)
        courseImporterService.importFromJson(term, params.json)
        flash.success = "Import succeeded; ${Course.count()} courses"
        redirect(action: "index")
    }
}
