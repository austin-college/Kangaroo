package kangaroo.data

import org.springframework.transaction.annotation.Transactional

class BackendDataService {

    def departmentDataService
    def majorDataService
    def requirementsDataService
    def facultyDataService
    def officeHoursDataService
    def staffDataService
    def cacheService

    @Transactional
    def upgradeAllIfNeeded() {

        println "\nUpgrading backend data..."
        departmentDataService.upgradeIfNeeded()
        majorDataService.upgradeIfNeeded()
        requirementsDataService.upgradeIfNeeded()
        facultyDataService.upgradeIfNeeded()
        officeHoursDataService.upgradeIfNeeded()
        staffDataService.upgradeIfNeeded()

        // Any changes?
        cacheService.initializeCacheIfNeeded()
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
}
