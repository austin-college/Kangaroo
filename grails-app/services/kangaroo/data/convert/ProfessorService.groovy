package kangaroo.data.convert

import kangaroo.Course
import kangaroo.Department
import kangaroo.Professor
import org.springframework.transaction.annotation.Transactional

import kangaroo.MeetingTime

/**
 * Contains utility methods for professors.
 */
class ProfessorService {

    // List staff names that should not appear in the colleagues list (@todo later just don't create these professors at all).
    final static def fakeStaffNames = ["STAFF", "No Information Available"]

    // List departments that many professors teach in (like Communication/Inquiry), and which are likely to dilute the colleagues list.
    final static def commonDepartments = ["CI"]

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
        String ids = redisService.memoize("professor/${professor.id}/colleagues") { def redis ->

            Set<Professor> colleagues = [];

            // O(n^4) goodness. Good thing we precache...
            professor.activeDepartments.each { dept ->
                Course.findAllByDepartment(dept).each { course ->
                    course.instructors.each { instructor ->

                        // Don't include certain staff names as "colleagues".
                        for (String fakeName: fakeStaffNames) {
                            if (instructor.name.contains(fakeName))
                                return;
                        }

                        colleagues << instructor
                    }
                }
            }

            // We are not our own colleague.
            colleagues.remove(professor);

            // Sort the list.
            def list = (colleagues as List);
            println list
            list = list.sort({a, b -> return a.name.compareTo(b.name)})

            // Extract and join the IDs together
            list*.id.join(",")
        }

        println ids

        // Transform the ID list back into a list of Professors.
        return ids.split(",").collect { id -> Professor.get(id)}.findAll { it != null }
    }

    /**
     * Returns a list of all departments that this professor teaches in.
     * Eg: if they teach a Chemistry class and a Biology class, it should return (Biology, Chemistry).
     */
    @Transactional(readOnly = true)
    List<Department> getDepartmentsForProfessor(Professor professor) {

        def departments = (professor.coursesTeaching*.department as Set);

        // Remove common departments (they dilute the colleagues list).
        commonDepartments.each { code -> departments.remove(Department.findByCode(code)); }

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

    /**
     * Returns the given list of courses, minus any that appear at the same time.
     */
    @Transactional(readOnly = true)
    static List<Course> filterSameTimeCourses(List<Course> courses) {

        List<Course> resultList = []
        def usedTimes = [];
        for (Course c: courses) {

            boolean rejected = false;
            for (MeetingTime m: c.meetingTimes) {
                if (usedTimes.contains(m)) {
                    rejected = true;
                    break;
                }
                else
                    usedTimes << m;
            }

            if (!rejected)
                resultList << c;
        }

        resultList;
    }
}
