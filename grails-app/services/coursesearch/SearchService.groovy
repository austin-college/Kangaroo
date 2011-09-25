package coursesearch

import grails.converters.JSON

class SearchService {

    static transactional = false

    def getCoursesTableJSON() {
        return getCoursesForTable() as JSON;
    }

    def getCoursesForTable() {
        def courses = Course.list()
        def formattedResults = courses.collect { course -> formatClass(course) }
        return ["sEcho": 0, "iTotalRecords": formattedResults.size(), "iTotalDisplayRecords": formattedResults.size(), "aaData": formattedResults]
    }

    def formatClass(Course course) {

        def row = []

        row << course.name
        row << course.department
        row << Teaching.findAllByCourse(course)*.professor*.name.join(' & ')
        row << course.department + ' ' + course.courseNumber + course.section
        row << course.capacity - course.seatsUsed
        row << course.schedule.trim()

        return row
    }
}
