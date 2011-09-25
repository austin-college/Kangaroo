package coursesearch

class Professor {

    static constraints = {
    }

    String name


    List<Course> getCoursesTeaching() { return Teaching.findAllByProfessor(this)*.course }
}
