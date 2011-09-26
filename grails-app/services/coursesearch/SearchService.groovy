package coursesearch

import grails.converters.JSON
import redis.clients.jedis.Jedis

class SearchService {

    def redisService

    static transactional = false

    String getCoursesTableCached() {
        redisService.memoize("courses") { Jedis redis -> coursesTableJSON };
    }

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
        row << course.department.name
        row << Teaching.findAllByCourse(course)*.professor*.name.join(' & ')
        row << course.department.code + ' ' + course.courseNumber + course.section
        row << course.capacity - course.seatsUsed
        row << course.schedule.trim()

        return row
    }
}
