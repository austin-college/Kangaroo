package kangaroo

import kangaroo.mn.CourseFulfillsRequirement
import kangaroo.mn.CourseMeetingTime
import kangaroo.mn.Teaching

/**
 * Represents a course taught at Austin College.
 */
class Course {

    String id

    static belongsTo = [term: Term]
    static hasMany = [textbooks: Textbook]

    def courseDataService

    int zap;
    boolean open;
    int capacity;
    int seatsUsed;
    boolean instructorConsentRequired;

    Department department // BIO
    int courseNumber // 652
    char section // A
    boolean isLab = false

    BigText description;
    String name;
    String room;
    String comments;

    boolean textbooksParsed

    static constraints = {
        id(size: 10..13) // (dept size) + (course number) + 6
        description(nullable: true)
    }

    static mapping = {
        textbooks(sort: "title", order: "asc")
        id(column: 'course_id', generator: 'assigned')
    }

    String textbookPageUrl() { "http://www.bkstr.com/webapp/wcs/stores/servlet/booklookServlet?sect-1=${section}&bookstore_id-1=239&term_id-1=${term.id}&div-1=&dept-1=${department.id}&course-1=${courseNumber}"}

    String generateIdString() { "${department.id}${courseNumber}${section}_${term.id}".toLowerCase() }

    String toString() { name }

    String sectionString() { department.id + ' ' + courseNumber + section; }

    List<Professor> getInstructors() { Teaching.findAllByCourse(this)*.professor; }

    List<Requirement> getRequirementsFulfilled() { CourseFulfillsRequirement.findAllByCourse(this)*.requirement; }

    List<MeetingTime> getMeetingTimes() { CourseMeetingTime.findAllByCourse(this)*.meetingTime }
}
