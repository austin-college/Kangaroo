package kangaroo.admin

class AdminDataController {

    def dataExportService

    def index = { }

    def runExport = {

        render(dataExportService.export());
    }
}
