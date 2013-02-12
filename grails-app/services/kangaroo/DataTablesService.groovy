package kangaroo
/**
 * Returns data formatted for the client-side DataTables.
 */
class DataTablesService {

    static transactional = false

    def cacheService

    def generateTableData(Term term, Requirement requirementToFulfill = null) {
        return AppUtils.runAndTime("Fetching table for $term...") {
            // Fetch the course IDs that will be in the table.
            Set<String> ids = fetchIdsForTable(term, requirementToFulfill);

            // Take the IDs and augment them into table rows.
            def rows = ids.collect { id ->
                cacheService.memoize("course/${id}/asRow") { formatIntoTableRow(Course.get(id)) }
            }

            return formatIntoTable(rows);
        }
    }

    /**
     * Fetches the IDs that will be in the table.
     */
    Set<String> fetchIdsForTable(Term term, Requirement requirementToFulfill = null) {
        return Course.executeQuery("select c.id from Course c, \
                            Term t where c.term = t and t.id = :termId", [termId: term.id])
    }

    /**
     * Formats the list of rows into a DataTables table.
     */
    def formatIntoTable(List courseRows) {
        return ["aaData": courseRows, "iTotalRecords": courseRows.size(), "iTotalDisplayRecords": courseRows.size(),
                "sEcho": 0];
    }

    /**
     * Formats a given course to fit in a table row.
     */
    def formatIntoTableRow(Course course) {

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
