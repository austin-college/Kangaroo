package kangaroo.data.convert

import kangaroo.*
import org.springframework.transaction.annotation.Transactional
/**
 * Contains utility methods for professors.
 */
class ProfessorService {

    // List staff names that should not appear in the colleagues list (@todo later just don't create these professors at all).
    final static def fakeStaffNames = ["STAFF", "No Information Available"]

    // List departments that many professors teach in (like Communication/Inquiry), and which are likely to dilute the colleagues list.
    final static def commonDepartments = ["CI"]

    // Stores/caches which departments a professor teaches in (maps a professor ID to a list of department IDs)
    static def peopleToDepartments = [:]

    // Stores/caches which professors are in a department (maps a department ID to a list of professor IDs)
    static def departmentToPeople = [:]

    def cacheService

    /**
     * Calculates/recalculates the peopleToDepartments and departmentToPeople maps.
     * This is a slow function generally taking ~2 seconds to run.
     */
    @Transactional(readOnly = true)
    def calculateRelatedProfessors() {

        peopleToDepartments = [:]
        departmentToPeople = [:]

        Course.currentCourses.each { course ->

            // Add this department to the professors' list...
            course.instructors.each {
                peopleToDepartments[it.id] = peopleToDepartments[it.id] ?: (Set) [] // Create an empty set first if it doesn't exist
                peopleToDepartments[it.id].add(course.department.id);
            }

            // Add these professors to the department's list...
            departmentToPeople[course.department.id] = departmentToPeople[course.department.id] ?: (Set) [] // Create an empty set first if it doesn't exist
            departmentToPeople[course.department.id].addAll(course.instructors.findAll { it.isActive }*.id);
        }
    }

    /**
     * Returns a list of all departments that this professor teaches in.
     * Eg: if they teach a Chemistry class and a Biology class, it should return [Biology, Chemistry].
     */
    @Transactional(readOnly = true)
    List<Department> getDepartmentsForProfessor(Professor professor) {

        // Return the pre-calculated version if one exists.
        if (peopleToDepartments[professor])
            return peopleToDepartments[professor].collect { Department.get(it) };

        def departments = (professor.coursesTeaching*.department as Set);

        // Remove common departments (they dilute the colleagues list).
        commonDepartments.each { code -> departments.remove(Department.get(code)); }

        // Sort the list, and save it.
        peopleToDepartments[professor] = (departments as List).sort({ a, b -> return a.name.compareTo(b.name) })*.id;
        return peopleToDepartments[professor].collect { Department.get(it) };
    }

    /**
     * Returns similar professors to this one, split by department.
     * Details: for every department this professor teaches in, get all the professors also in that department. No professor will be repeated.
     *
     * ex: [Chemistry: [Andrew Carr, Bradley Smucker, ...],
     *      Biology: [Peter Schulz, George Diggs, ...],
     *      Math: [J'Lee Bumpus, E'Don Williams, ...]
     *     ]
     * for a professor who teaches in Chemistry, Biology, and Math.
     */
    @Transactional(readOnly = true)
    Map<Department, List<Professor>> getRelatedProfessorsForProfessor(Professor professor) {

        def data = [:]
        def seenIds = [professor.id] // Don't include yourself.
        professor.activeDepartments.each { dept ->

            def ids = departmentToPeople[dept.id].findAll { !seenIds.contains(it) }
            def people = ids.collect { Professor.get(it) }

            // Don't repeat professors.
            seenIds.addAll(ids)

            if (people.size() > 0) // Don't add empty departments.
                data[dept] = people;
        }

        return data;
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
        return (rooms as List).sort({ a, b -> return a.compareTo(b) });
    }

    /**
     * Returns the given list of courses, minus any that appear at the same time.
     */
    @Transactional(readOnly = true)
    static List<Course> filterSameTimeCourses(List<Course> courses) {

        List<Course> resultList = []
        def usedTimes = [];
        for (Course c : courses) {

            boolean rejected = false;
            for (MeetingTime m : c.meetingTimes) {
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

    /**
     * Returns whether the given professor is having office hours, RIGHT NOW.
     * @todo [PC] Support future dates (remember to align the weeks).
     */
    @Transactional(readOnly = true)
    static boolean isInOfficeHours(Professor professor) {

        for (def time : ScheduleProjectService.projectToWeek(professor.officeHours))
            if (AppUtils.isDateBetween(new Date(), time.startDate, time.endDate))
                return true;

        return false;
    }

    /**
     * Returns the class the professor is teaching RIGHT NOW (if any).
     * @todo [PC] Support future dates (remember to align the weeks).
     */
    @Transactional(readOnly = true)
    static Course getCurrentClass(Professor professor) {

        for (def course : professor.currentCursesTeaching)
            for (def time : ScheduleProjectService.projectToWeek(course.meetingTimes))
                if (AppUtils.isDateBetween(new Date(), time.startDate, time.endDate))
                    return course;
    }

    /**
     * Returns the status of the given professor (teaching, in office hours, unknown) RIGHT NOW.
     *
     * @return A map with these keys: [status, available, busy].
     *  status: "officeHours", "inClass", "unknown"
     *  available: true only if the professor is confirmed to be available
     *  busy: true only if the professor is confirmed to be busy
     *
     * @todo [PC] Support future dates (remember to align the weeks).
     */
    @Transactional(readOnly = true)
    static getStatus(Professor professor) {

        // Are they having office hours?
        if (isInOfficeHours(professor))
            return [status: "officeHours", available: true]
        else {

            // See if they're in a class.
            def course = getCurrentClass(professor)
            if (course)
                return [status: "inClass", course: course, busy: true]
        }

        return [status: "unknown"]
    }
}
