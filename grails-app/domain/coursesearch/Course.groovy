package coursesearch

import coursesearch.mn.CourseFulfillsRequirement
import coursesearch.mn.CourseMeetingTime
import coursesearch.mn.Teaching

/**
 * Represents a course taught at Austin College.
 */
class Course {

    static belongsTo = [term: Term]
    static hasMany = [textbooks: Textbook]

    def courseDataService

    boolean open;
    int capacity;
    int seatsUsed;
    boolean instructorConsentRequired;

    Department department // BIO
    int courseNumber // 652
    char section // A
    boolean isLab = false

    String name;
    String description
    String room;
    String comments;

    boolean textbooksParsed

    static mapping = {
        textbooks(sort: "title", order: "asc")
        id(generator: 'assigned')
    }

    static constraints = {
        description(maxSize: 16384)
    }

    String textbookPageUrl() { "http://www.bkstr.com/webapp/wcs/stores/servlet/booklookServlet?sect-1=${section}&bookstore_id-1=239&term_id-1=${term.shortCode}&div-1=&dept-1=${department.code}&course-1=${courseNumber}"}

    String toString() { name }

    String sectionString() { department.code + ' ' + courseNumber + section; }

    List<Professor> getInstructors() { Teaching.findAllByCourse(this)*.professor; }

    List<Requirement> getRequirementsFulfilled() { CourseFulfillsRequirement.findAllByCourse(this)*.requirement; }

    List<MeetingTime> getMeetingTimes() { CourseMeetingTime.findAllByCourse(this)*.meetingTime }
}
