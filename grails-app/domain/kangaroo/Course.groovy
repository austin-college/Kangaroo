package kangaroo

import kangaroo.data.convert.ScheduleConvertService
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

    static Course saveFromJsonObject(object, Term term) { // @todo eliminate reliance on Term here
        if (Course.get(object.id))
            return Course.get(object.id)

        def course = new Course(name: object.name, description: BigText.getOrCreate(AppUtils.fixFakeNull(object.description)), zap: object.zap, open: object.open,
                capacity: object.capacity, isLab: object.isLab, hasLabs: object.hasLabs, instructorConsentRequired: object.instructorConsentRequired,
                department: Department.saveFromJsonObject(object.department), courseNumber: object.courseNumber, section: object.section.toString(),
                room: object.room, comments: object.comments, term: term);

        course.id = object.id;
        course = (Course) AppUtils.saveSafely(course);

        // Save m-n classes too.
        object.requirementsFulfilled.each { AppUtils.saveSafely(new CourseFulfillsRequirement(course: course, requirement: Requirement.saveFromJsonObject(it))) }
        object.meetingTimes.each { AppUtils.saveSafely(new CourseMeetingTime(course: course, meetingTime: ScheduleConvertService.convertMeetingTime(it)?.saveOrFind())) }
        object.instructors.each { AppUtils.saveSafely(new Teaching(course: course, professor: Professor.get(it.id))) }

        return course;
    }

    def toJsonObject() {
        [id: id, name: name, description: description?.description, zap: zap, open: open,
                capacity: capacity, isLab: isLab, hasLabs: hasLabs, instructorConsentRequired: instructorConsentRequired,
                department: department, courseNumber: courseNumber, section: section.toString(), room: room, meetingTimes: meetingTimes*.toString(),
                comments: comments, term: term.id, requirementsFulfilled: requirementsFulfilled, instructors: instructors.collect { [id: it.id, name: it.name, email: it.email, title: it.title, photoUrl: it.photoUrl] }
        ];
    }
}
