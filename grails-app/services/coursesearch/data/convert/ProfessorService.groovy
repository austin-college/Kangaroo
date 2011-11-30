package coursesearch.data.convert

import coursesearch.Course
import coursesearch.Department
import coursesearch.Professor
import org.springframework.transaction.annotation.Transactional

/**
 * Contains utility methods for professors.
 */
class ProfessorService {

    def redisService

    /**
     * Returns a list of this professor's "colleagues". Since professors are not directly mapped to departments (yet!),
     * but classes are, we return a list of all professors who teach in the same departments you do. (A set forest)
     *
     * Naturally once we parse department strings, this will all be unnecessary.
     */
    @Transactional(readOnly = true)
    List<Professor> getColleaguesForProfessor(Professor professor) {

        // This query is slow, so we cache the results in redis.
        def ids = (List<Long>) redisService.memoizeDomainIdList(Professor, "professor/${professor.id}/colleagues") { def redis ->

            Set<Professor> colleagues = [];

            // O(n^3) goodness...
            professor.activeDepartments.each { dept ->
                Course.findAllByDepartment(dept).each { course ->
                    course.instructors.each { instr ->
                        if (!instr.name.contains("STAFF"))
                            colleagues << instr
                    }
                }
            }

            // We are not our own colleague.
            colleagues.remove(this);

            // Sort the list.
            return ((colleagues as List).sort({a, b -> return a.name.compareTo(b.name)}));
        }

        // Transform the ID list back into a list of Professors.
        return ids.collect { id -> Professor.get(id)}
    }

    /**
     * Returns a list of all departments that this professor teaches in.
     * Eg: if they teach a Chemistry class and a Biology class, it should return (Biology, Chemistry).
     */
    @Transactional(readOnly = true)
    List<Department> getDepartmentsForProfessor(Professor professor) {

        def departments = (professor.coursesTeaching*.department as Set);
        departments.remove(Department.findByCode("CI"));

        // Sort the list.
        return (departments as List).sort({a, b -> return a.name.compareTo(b.name)});
    }

    /**
     * Returns a list of all rooms that this professor teaches in.
     * Eg: if they teach Chemistry in MS 301 and Biology in MS 200, it should return ("MS 301", "MS 200").
     */
    @Transactional(readOnly = true)
    List<String> getRoomsForProfessor(Professor professor) {

        def rooms = (professor.coursesTeaching*.room as Set);
        rooms.remove("");

        // Sort the list.
        return (rooms as List).sort({a, b -> return a.compareTo(b)});
    }
}
