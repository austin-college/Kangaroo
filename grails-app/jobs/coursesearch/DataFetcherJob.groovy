package coursesearch

class DataFetcherJob {

    def backendDataService

    static triggers = {
        simple name: 'mySimpleTrigger', startDelay: 2 * 60 * 1000, repeatInterval: 60 * 1000
    }

    def execute() { backendDataService.upgradeAllIfNeeded() }
}
