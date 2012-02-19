package kangaroo

class DataFetcherJob {

    def backendDataService

    static triggers = {
        simple name: 'every15Minutes', startDelay: 2 * 60 * 1000, repeatInterval: 60 * 1000 * 15
    }

    def execute() { backendDataService.upgradeAllIfNeeded() }
}
