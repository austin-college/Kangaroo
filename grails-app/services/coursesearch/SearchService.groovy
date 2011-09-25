package coursesearch

import grails.converters.JSON

class SearchService {

    static transactional = false

    def getCoursesForTable(params) {

        def classes;
        if (params.s) {
            classes = Course.withCriteria {
                ilike("name", "%${params.s}%")
            }
        }
        else
            classes = Course.list()

        def formattedResults = formatForTable(classes);
        return ["sEcho": 0, "iTotalRecords": formattedResults.size(), "iTotalDisplayRecords": formattedResults.size(), "aaData": formattedResults]
    }

    def formatForTable(List<Course> notes) {
        notes.collect { note -> formatClass(note) }
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
