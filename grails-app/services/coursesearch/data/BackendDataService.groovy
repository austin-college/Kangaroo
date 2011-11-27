package coursesearch.data

import org.springframework.transaction.annotation.Transactional
import coursesearch.Professor

class BackendDataService {

    def majorDataService
    def requirementsDataService
    def facultyDataService
    def officeHoursDataService

    @Transactional
    def upgradeAllIfNeeded() {

        println "\nUpgrading backend data..."
        majorDataService.upgradeIfNeeded()
        requirementsDataService.upgradeIfNeeded()

        if (Professor.count() == 0)
            facultyDataService.fetchAndMatch()

        officeHoursDataService.upgradeIfNeeded()

    }
}
