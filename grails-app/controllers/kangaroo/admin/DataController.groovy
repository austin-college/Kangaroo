package kangaroo.admin

class DataController {

    def dataExportService

    def index = { }

    def runExport = {

        render(dataExportService.export());
    }
}
