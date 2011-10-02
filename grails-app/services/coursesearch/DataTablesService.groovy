package coursesearch

import grails.converters.JSON
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

        def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()
        row << "<a href='${g.createLink(controller: 'course', action: 'show', id: course.zap)}'>${course}</a>"
        row << course.department.name
        row << course.instructors.collect { "<a href='${g.createLink(controller: 'professor', action: 'show', id: it.id)}'>${it}</a>"}.join(' & ')
        row << course.sectionString()
        row << course.capacity - course.seatsUsed
        row << course.schedule.trim()

        return row;
    }
}
