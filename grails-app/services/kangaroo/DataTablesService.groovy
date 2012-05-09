package kangaroo

import grails.converters.JSON

/**
 * Returns data formatted for the client-side DataTables.
 */
class DataTablesService {

    static transactional = false

    def cacheService

    /**
     * Formats all of the courses into a table. (SLOW)
     * @todo speed up
     */
    def getTable(Term term) {
        def rows = Course.findAllByTerm(term).collect { course -> toRow(course) }
        return (["sEcho": 0, "iTotalRecords": rows.size(), "iTotalDisplayRecords": rows.size(), "aaData": rows] as JSON)
    }

    /**
     * Returns a cached version of the table.
     */
    String getTableCached(Term term) {
        cacheService.memoize("courses/${term.id}") { getTable(term) }
    }

    /**
     * Formats a given course to fit in a table row.
     */
    def toRow(Course course) {

        def row = []

        row << "<a href='${AppUtils.createLink('course', course.id)}'>${course}</a> <span class='section'>${course.sectionString()}</span>"
        row << course.department.name
        if (course.instructors)
            row << AppUtils.getProfessorLinksForClass(course, false, "<br/>");
        else
            row << "<i>Unknown<i/>"

        if (course.meetingTimes)
            row << AppUtils.getScheduleLinksForClass(course)
        else
            row << "<i>Unknown<i/>"

        return row;
    }
}
