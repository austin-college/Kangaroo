package coursesearch

import coursesearch.mn.ProfessorOfficeHours
import coursesearch.mn.Teaching

/**
 * The facilitators of learning, the lifeblood of a university, the people who hate grading.
 */
class Professor {

    def professorService

    String id
    String name

    /**
     * Has this professor been matched with the faculty data from github.com/austin-college/data?
     * If so -> we will have their photo, title, office location, phone number, and department.
     * If not -> this means they were listed in the course catalog as teaching a class, but not on the austin-college website.
     */
    boolean matched = false

    String photoUrl
    String title
    String department
    String office
    String phone
    String email

    static constraints = {
        id(maxSize: 32, blank: false)
        photoUrl(nullable: true)
        title(nullable: true)
        department(nullable: true)
        office(nullable: true)
        phone(nullable: true)
        email(nullable: true)
    }

    static mapping = {
        id(column: 'user_id', generator: 'assigned')
    }

    String toString() { name }

    /**
     * Returns all of the courses this professor is teaching. (Not limited by term)
     */
    List<Course> getCoursesTeaching() { return Teaching.findAllByProfessor(this)*.course }

    /**
     * Returns this professor's office hours.
     */
    List<MeetingTime> getOfficeHours() { return ProfessorOfficeHours.findAllByProfessor(this)*.meetingTime }

    /**
     * Returns this professor's colleagues (professors who teach in the same departments this guy does).
     */
    List<Professor> getColleagues() { professorService.getColleaguesForProfessor(this) }

    /**
     * Returns all of the departments this professor teaches classes in (ie, [Biology, Chemistry]).
     */
    List<Department> getActiveDepartments() { professorService.getDepartmentsForProfessor(this) }

    /**
     * Returns all of the rooms this professor teaches classes in (ie, ["MS128", "MS133"]).
     */
    List<String> getActiveRooms() { professorService.getRoomsForProfessor(this)}
}
