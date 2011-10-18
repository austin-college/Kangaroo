package coursesearch

import grails.converters.JSON

class BatchControlController {

    def courseDataService
    def facultyDataService

    def index = {
        [facultyDetails: getFacultyDetails(), classDetails: getCourseDetails(), textbookDetails: getTextbookDetails()]
    }

    def reimportCourses = {

        def time = CourseUtils.time { courseDataService.downloadAndProcess() }
        render([success: true, time: time, details: getCourseDetails()] as JSON)
    }

    def rematchFaculty = {

        def time = CourseUtils.time { facultyDataService.fetchAndMatch(); }
        render([success: true, time: time, details: getFacultyDetails()] as JSON)
    }

    def fetchTextbooks = {

    }

    def getFacultyDetails() {
        def percentFacultyMatched = (double) (100 * Professor.countByMatched(true) / Professor.count());
        [numFaculty: Professor.count(), numMatched: Professor.countByMatched(true), percentMatched: percentFacultyMatched.round()]
    }

    def getCourseDetails() {
        [numCourses: Course.count()]
    }

    def getTextbookDetails() {
        def percentCourses = toPercent(Course.countByTextbooksParsed(true) / Course.count());
        def percentAmazonDetails = toPercent(Textbook.countByMatchedOnAmazon(true) / Textbook.count());
        [numTextbooks: Textbook.count(), percentCoursesWithBooks: percentCourses, percentLookedUp: percentAmazonDetails]

    }

    int toPercent(value) {
        (int) ((100.0 * (double) value).round())
    }
}
