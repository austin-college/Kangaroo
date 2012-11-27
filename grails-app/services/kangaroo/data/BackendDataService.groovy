package kangaroo.data

import kangaroo.AppUtils
import kangaroo.Professor
import org.springframework.transaction.annotation.Transactional

class BackendDataService {

    def departmentDataService
    def majorDataService
    def requirementsDataService
    def facultyDataService
    def rooRouteDataService
    def officeHoursDataService
    def staffDataService
    def professorService

    @Transactional
    def upgradeAllIfNeeded() {

        println "\nUpgrading backend data..."
        try {
            departmentDataService.upgradeIfNeeded()
            majorDataService.upgradeIfNeeded()
            requirementsDataService.upgradeIfNeeded()

            if (Professor.count() == 0)
                facultyDataService.upgradeIfNeeded()
            officeHoursDataService.upgradeIfNeeded()
            staffDataService.upgradeIfNeeded()
            rooRouteDataService.upgradeIfNeeded()
        }
        catch (UnknownHostException) {
            println "Couldn't connect to github data."
        }
        AppUtils.runAndTime("Calculated related professors") {
            professorService.calculateRelatedProfessors();
        }
    }

    def reset() {
        println "\nResetting backend data version..."
        departmentDataService.lastVersionUsed = 0
        requirementsDataService.lastVersionUsed = 0
        majorDataService.lastVersionUsed = 0
        facultyDataService.lastVersionUsed = 0
        officeHoursDataService.lastVersionUsed = 0
        rooRouteDataService.lastVersionUsed = 0
        upgradeAllIfNeeded()
    }
}
