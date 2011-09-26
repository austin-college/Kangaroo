package coursesearch

class Professor {

    static constraints = {
    }

    String name

    String toString() { name }

    List<Course> getCoursesTeaching() { return Teaching.findAllByProfessor(this)*.course }
}
