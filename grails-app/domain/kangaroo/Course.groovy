package kangaroo

import kangaroo.mn.CourseFulfillsRequirement
import kangaroo.mn.CourseMeetingTime
import kangaroo.mn.Teaching

/**
 * Represents a course taught at Austin College.
 */
class Course {

    static belongsTo = [term: Term]
    static hasMany = [textbooks: Textbook]
    def courseDataService

    String id
    String name;
    BigText description;
    int zap;

    boolean open;
    int capacity;
    int seatsUsed;
    boolean instructorConsentRequired;

    Department department // BIO
    int courseNumber // 652
    char section // A
    boolean isLab = false
    boolean hasLabs = false

    String room;
    String comments;

    Date dateTextbooksParsed

    static constraints = {
        id(size: 10..17) // (dept size) + (course number) + 6
        description(nullable: true)
        name(maxSize: 64)
        room(maxSize: 32)
        comments(maxSize: 512)
        dateTextbooksParsed(nullable: true)
    }

    static mapping = {
        textbooks(sort: "title", order: "asc")
        id(column: 'course_id', generator: 'assigned')
    }

    // Generates a universal ID string for the given attributes.
    static String generateIdString(Term term, Department department, int courseNumber, char section) { "${department.id}${courseNumber}${section}_${term.id}".toLowerCase() }

    // Fetches a specific course efficiently.
    static Course get(Term term, Department department, int courseNumber, char section) { Course.get(generateIdString(term, department, courseNumber, section)); }

    // Fetches all of the sections of a specific course efficiently.
    static List<Course> findAllSections(Term term, Department department, int courseNumber) {
        ('A'..'Z').collect { section -> Course.get(term, department, courseNumber, (char) section) }.findAll { it }
    }

    // Returns all courses in the current term.
    static List<Course> getCurrentCourses() { Course.findAllByTerm(Term.currentTerm) }

    // Fetches all of this course's sibling sections efficiently.
    List<Course> getSiblings() { findAllSections(term, department, courseNumber) }

    String generateIdString() { generateIdString(term, department, courseNumber, section) }

    String toString() { name }

    String sectionString() { department.id + ' ' + courseNumber + section; }

    List<Professor> getInstructors() { Teaching.findAllByCourse(this)*.professor; }

    List<Requirement> getRequirementsFulfilled() { CourseFulfillsRequirement.findAllByCourse(this)*.requirement; }

    List<MeetingTime> getMeetingTimes() { CourseMeetingTime.findAllByCourse(this)*.meetingTime }

    String textbookPageUrl() { "http://www.bkstr.com/webapp/wcs/stores/servlet/booklookServlet?sect-1=${section}&bookstore_id-1=239&term_id-1=${term.id}&div-1=&dept-1=${department.id}&course-1=${courseNumber}"}

    def toJson() {
        [id: this.id, name: this.name, description: this.description?.description, zap: this.zap, open: this.open,
                capacity: this.capacity, isLab: this.isLab, hasLabs: this.hasLabs, instructorConsentRequired: this.instructorConsentRequired,
                department: this.department, courseNumber: this.courseNumber, section: this.section.toString(), room: this.room, meetingTimes: this.meetingTimes*.toString(),
                comments: this.comments, requirementsFulfilled: requirementsFulfilled, instructors: instructors.collect { [id: it.id, name: it.name, email: it.email, title: it.title, photoUrl: it.photoUrl] }
        ];
    }
}
