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
        return courseIds.collect { id ->
            cacheService.memoize("course/${id}/asRowObj") {
                formatIntoTableRow(Course.get(id))
            }
        }
    }

    /**
     * Formats a course into a table row.
     */
    def formatIntoTableRow(Course course) {
        return [
                course.id,
                course.name,
                [course.department.id, course.department.name],
                course.instructors.collect { [it.id, it.name] },
                course.meetingTimes.collect { [it.id, it.toString()] },
                course.requirementsFulfilled*.id
        ];
    }
}
