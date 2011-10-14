package coursesearch

import grails.converters.JSON

class BatchControlController {

    def facultyDataService

    def index = {
        [facultyDetails: getFacultyDetails()]
    }

    def rematchFaculty = {

        facultyDataService.fetchAndMatch();

        render([success: true, details: getFacultyDetails()] as JSON)
    }

    def getFacultyDetails() {
        def percentFacultyMatched = (double) (100 * Professor.countByMatched(true) / Professor.count());
        [numFaculty: Professor.count(), numMatched: Professor.countByMatched(true), percentMatched: percentFacultyMatched.round()]
    }
}
