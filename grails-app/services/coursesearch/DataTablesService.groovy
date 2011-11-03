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
    def getTable(term) {
        def rows = Course.findAllByTerm(term).collect { course -> toRow(course) }
        return (["sEcho": 0, "iTotalRecords": rows.size(), "iTotalDisplayRecords": rows.size(), "aaData": rows] as JSON)
    }

    /**
     * Returns a redis-cached version of the table.
     */
    String getTableCached(term) { redisService.memoize("courses/$term") { Jedis redis -> getTable(term) } }

    /**
     * Formats a given course to fit in a table row.
     */
    def toRow(Course course) {

        def row = []

        row << "<a href='${CourseUtils.createLink('course', 'show', course.id)}'>${course}</a> <span class='section'>${course.sectionString()}</span>"
        row << course.department.name
        row << CourseUtils.getProfessorLinksForClass(course, true, "<br/>");
        row << CourseUtils.getScheduleLinksForClass(course)

        return row;
    }
}
