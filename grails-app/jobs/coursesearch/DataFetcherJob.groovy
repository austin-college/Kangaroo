package coursesearch

class DataFetcherJob {

    def requirementsDataService
    def majorDataService

    static triggers = {
        simple name: 'mySimpleTrigger', startDelay: 2 * 60000, repeatInterval: 60000
    }

    def execute() {
        
        println "Upgrading backend data..."
        majorDataService.upgradeIfNeeded()
        requirementsDataService.upgradeIfNeeded()
    }
}
