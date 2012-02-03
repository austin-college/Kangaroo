package coursesearch.data

import org.springframework.transaction.annotation.Transactional
import coursesearch.Professor

class BackendDataService {

    def departmentDataService
    def majorDataService
    def requirementsDataService
    def facultyDataService

    def dataExportService

    @Transactional
    def upgradeAllIfNeeded() {

        println "\nUpgrading backend data..."
        departmentDataService.upgradeIfNeeded()
        majorDataService.upgradeIfNeeded()
        requirementsDataService.upgradeIfNeeded()

        if (Professor.count() == 0)
            facultyDataService.fetchAndMatch()

        dataExportService.exportOfficeHours()
    }

    def reset() {
        println "\nResetting backend data version..."
        DepartmentDataService.lastVersionUsed = 0
        RequirementsDataService.lastVersionUsed = 0
        MajorDataService.lastVersionUsed = 0
    }
}
