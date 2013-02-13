package kangaroo
/**
 * Returns data formatted for the client-side DataTables.
 */
class DataTablesService {

    static transactional = false
    def cacheService

    // Queries that are used to fetch tables.
    static class Query {
        Term term;
        Requirement requirementToFulfill; // (optional)

        String toString() { "$term" }
    }

    def generateTableData(Query query) {
        return AppUtils.runAndTime("Fetching table for $query...") {
            List<String> ids = fetchIdsForQuery(query);
            return expandIntoTable(ids);
        }
    }

    /**
     * Runs the query and returns the course IDs that will be in the table.
     */
    List<String> fetchIdsForQuery(Query query) {
        return Course.executeQuery("select c.id from Course c, Term t \
                 where c.term = t and t.id = :termId", [termId: query.term.id])
    }

    /**
     * Creates a full DataTables table from a list of course IDs.
     */
    def expandIntoTable(List<String> courseIds) {
        // Turn each ID into a table row.
        def tableRows = courseIds.collect { id ->
            cacheService.memoize("course/${id}/asRow") {
                formatIntoTableRow(Course.get(id))
            }
        }

        return ["aaData": tableRows,
                "iTotalRecords": tableRows.size(),
                "iTotalDisplayRecords": tableRows.size(),
                "sEcho": 0];
    }

    /**
     * Formats a course into a table row.
     */
    def formatIntoTableRow(Course course) {
        def row = []

        row << "<a href='${AppUtils.createLink('course', course.id)}'>${course}</a>" +
                "<span class='section'>${course.sectionString()}</span>"
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
