package coursesearch.data

import org.springframework.transaction.annotation.Transactional
import coursesearch.Professor

class BackendDataService {

    def departmentDataService
    def majorDataService
    def requirementsDataService
    def facultyDataService
    def officeHoursDataService

    @Transactional
    def upgradeAllIfNeeded() {

        println "\nUpgrading backend data..."
        departmentDataService.upgradeIfNeeded()
        majorDataService.upgradeIfNeeded()
        requirementsDataService.upgradeIfNeeded()

        if (Professor.count() == 0)
            facultyDataService.fetchAndMatch()

        officeHoursDataService.upgradeIfNeeded()

    }
}
