package coursesearch

import coursesearch.mn.CourseFulfillsRequirement

class Requirement {

    String code
    String name
    boolean isInterdisciplinaryMajor

    static constraints = {
        code(maxSize: 8)
        name(maxSize: 96)
    }

    List<Course> getCoursesThatFulfill() { CourseFulfillsRequirement.findAllByRequirement(this)*.course; }

    String toString() { name }
}
