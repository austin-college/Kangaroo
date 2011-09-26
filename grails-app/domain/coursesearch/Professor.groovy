package coursesearch

class Professor {

    String name

    boolean matched = false
    String photoUrl
    String title
    String department
    String office
    String phone
    String email

    static constraints = {
        photoUrl(nullable: true)
        title(nullable: true)
        department(nullable: true)
        office(nullable: true)
        phone(nullable: true)
        email(nullable: true)
    }

    String toString() { name }

    List<Course> getCoursesTeaching() { return Teaching.findAllByProfessor(this)*.course }
}
