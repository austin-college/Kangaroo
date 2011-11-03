package coursesearch.admin

import coursesearch.Course

class DataImportController {

    def courseImporterService

    def index = {}

    def submit = {
        courseImporterService.importFromJson(params.json)
        flash.success = "Import succeeded; ${Course.count()} courses"
        redirect(action: "index")
    }
}
