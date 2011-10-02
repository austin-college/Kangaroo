package coursesearch

import grails.converters.JSON
import grails.util.Environment
import redis.clients.jedis.Jedis

/**
 * Returns data formatted for the client-side DataTables.
 */
class DataTablesService {

    static transactional = false

    def redisService

    /**
     * Formats all of the courses into a table.
     */
    def getTable() {
        def rows = Course.list().collect { course -> toRow(course) }
        return (["sEcho": 0, "iTotalRecords": rows.size(), "iTotalDisplayRecords": rows.size(), "aaData": rows] as JSON)
    }

    /**
     * Returns a redis-cached version of the table.
     */
    String getTableCached() { redisService.memoize("courses") { Jedis redis -> getTable() } }

    /**
     * Formats a given course to fit in a table row.
     */
    def toRow(Course course) {

        def row = []

        row << "<a href='${createLink('course', 'show', course.zap)}'>${course}</a>"
        row << course.department.name
        row << course.instructors.collect { "<a href='${createLink('professor', 'show', it.id)}'>${it}</a>"}.join(' & ')
        row << course.sectionString()
        row << course.capacity - course.seatsUsed
        row << course.schedule.trim()

        return row;
    }

    String createLink(controller, action, id) {

        def prefix = (Environment.current == Environment.PRODUCTION) ? "http://csac.austincollege.edu/courses" : "http://localhost:8080/CourseSearch";
        return "${prefix}/${controller}/${action}/${id}";
    }
}
