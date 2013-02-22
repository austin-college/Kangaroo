package kangaroo

/**
 * Calculates data for the course table.
 */
class DataTablesService {

    static transactional = false
    def cacheService

    // Queries that are used to fetch tables.
    static class Query {
        Term term;

        String toString() { "$term" }
    }

    /**
     * Returns the table for the given query.
     */
    def generateTableData(Query query) {
        return AppUtils.runAndTime("Fetching table for $query...") {
            List<String> ids = fetchIdsForQuery(query);
            return expandIntoTable(ids);
        }
    }

    /**
     * Returns the course IDs that will be in the table for the given query.
     */
    List<String> fetchIdsForQuery(Query query) {
        return Course.executeQuery("select c.id from Course c, Term t \
                 where c.term = t and t.id = :termId", [termId: query.term.id])
    }

    /**
     * Turns a list of course IDs into a list of row objects.
     */
    def expandIntoTable(List<String> courseIds) {
        return courseIds.collect { id ->
            cacheService.memoize("course/${id}/asRowObj") {
                formatIntoTableRowObject(Course.get(id))
            }
        }
    }

    /**
     * Formats a course into a row object.
     */
    def formatIntoTableRowObject(Course course) {
        /*
         NOTE: These used to be full-fledged JSON objects, but it's significantly more compact to send them as arrays,
         then re-inflate them into objects on the client side. So, the index of each element is important.
         */
        return [
                course.id,
                course.sectionString(),
                course.name,
                [course.department.id, course.department.name],
                course.instructors.collect { [it.id, it.name] },
                course.meetingTimes.collect { [it.id, it.toString()] },
                course.requirementsFulfilled*.id
        ];
    }
}
