package coursesearch

import grails.util.Environment

/**
 * Useful stuff.
 */
public class CourseUtils {

    /**
     * Removes all but someone's first and last name.
     */
    static String cleanFacultyName(String name) {

        // Remove trailing whitespace and "Dr.".
        def processed = name.trim().replaceAll("Dr\\. ", "").trim();

        // Remove any middle initials.
        def words = processed.split(" ");
        if (words.size() == 3 && words[2].length() == 2)
            return words[0] + " " + words[-1];
        else
            processed
    }

    static String getProfessorLinksForClass(Course course, String connector = ' & ') {
        course.instructors.collect { "<a href='${createLink('professor', 'show', it.id)}'>${it}</a>"}.join(connector)
    }

    static String getRoomLinksForProfessor(Professor professor, String connector = ', ') {
        professor.activeRooms.collect { room -> "<a href='${createLink('course', 'byRoom', room)}'>${room.trim()}</a>"}.join(connector)
    }

    static String createLink(controller, action, id) {

        def prefix = (Environment.current == Environment.PRODUCTION) ? "http://csac.austincollege.edu/courses" : "http://localhost:8080/CourseSearch";
        return "${prefix}/${controller}/${action}/${id}";
    }
}