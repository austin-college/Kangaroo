package coursesearch

import grails.converters.JSON
import redis.clients.jedis.Jedis

class SearchService {

    def redisService

    static transactional = false

    String getCoursesTableCached() {
        //coursesTableJSON
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

        def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()
        row << "<a href='${g.createLink(controller:'course', action:'show', id: course.zap, absolute:true)}'>${course}</a>"
        row << course.department.name
        row << course.instructors.collect { "<a href='${g.createLink(controller:'professor', action:'show', id: it.id, absolute:true)}'>${it}</a>"}.join(' & ')
        row << course.department.code + ' ' + course.courseNumber + course.section
        row << course.capacity - course.seatsUsed
        row << course.schedule.trim()

        return row
    }
}
