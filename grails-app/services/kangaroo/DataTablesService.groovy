package kangaroo

import grails.converters.JSON

/**
 * Returns data formatted for the client-side DataTables.
 */
class DataTablesService {

    static transactional = false

    def cacheService

    /**
     * Formats all of the courses into a table. (slowish)
     */
    def getTable(Term term) {
        def ids = Course.executeQuery("select c.id from Course c, Term t where c.term = t and t.id = :termId", [termId: term.id]);
        def rows = ids.collect { id -> cacheService.memoize("course/${id}/asRow") { toRow(Course.get(id)) } }
        return (["sEcho": 0, "iTotalRecords": rows.size(), "iTotalDisplayRecords": rows.size(), "aaData": rows] as JSON)
    }

    /**
     * Returns a cached version of the table.
     */
    String getTableCached(Term term) {
        def courses = []
        AppUtils.runAndTime("Fetching table for $term...") { courses = getTable(term) }
        return courses
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
