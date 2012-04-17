package kangaroo.data

import kangaroo.Term
import kangaroo.data.prefill.FacultyDataService
import org.springframework.transaction.annotation.Transactional

class BackendDataService {

    // The current term.
    static final String CURRENT_TERM_CODE = "12SP"

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
        facultyDataService.upgradeIfNeeded()
        officeHoursDataService.upgradeIfNeeded()
    }

    def reset() {
        println "\nResetting backend data version..."
        departmentDataService.lastVersionUsed = 0
        requirementsDataService.lastVersionUsed = 0
        majorDataService.lastVersionUsed = 0
        facultyDataService.lastVersionUsed = 0
        officeHoursDataService.lastVersionUsed = 0
        upgradeAllIfNeeded()
    }

    static Term getCurrentTerm() { return Term.findOrCreate(CURRENT_TERM_CODE)}
}
