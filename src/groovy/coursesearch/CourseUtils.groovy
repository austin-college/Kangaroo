package coursesearch

import grails.util.Environment

/**
 * Useful stuff.
 */
public class CourseUtils {

    static String getProfessorLinksForClass(Course course, String connector = ' & ') {
        course.instructors.collect { "<a href='${createLink('professor', 'show', it.id)}'>${it}</a>"}.join(connector)
    }

    static String getRoomLinksForProfessor(Professor professor, String connector = ', ') {
        professor.activeRooms.collect { room-> "<a href='${createLink('course', 'byRoom', room)}'>${room.trim()}</a>"}.join(connector)
    }

    static String createLink(controller, action, id) {

        def prefix = (Environment.current == Environment.PRODUCTION) ? "http://csac.austincollege.edu/courses" : "http://localhost:8080/CourseSearch";
        return "${prefix}/${controller}/${action}/${id}";
    }
}