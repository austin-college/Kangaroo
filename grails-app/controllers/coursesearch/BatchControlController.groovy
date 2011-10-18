package coursesearch

import grails.converters.JSON

class BatchControlController {

    def courseDataService
    def facultyDataService

    def index = {
        [facultyDetails: getFacultyDetails(), classDetails: getCourseDetails()]
    }

    def reimportCourses = {

        def time = CourseUtils.time { courseDataService.downloadAndProcess() }
        render([success: true, time: time, details: getCourseDetails()] as JSON)
    }

    def rematchFaculty = {

        def time = CourseUtils.time { facultyDataService.fetchAndMatch(); }
        render([success: true, time: time, details: getFacultyDetails()] as JSON)
    }

    def getFacultyDetails() {
        def percentFacultyMatched = (double) (100 * Professor.countByMatched(true) / Professor.count());
        [numFaculty: Professor.count(), numMatched: Professor.countByMatched(true), percentMatched: percentFacultyMatched.round()]
    }

    def getCourseDetails() {
        [numCourses: Course.count()]
    }
}
