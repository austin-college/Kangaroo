package coursesearch.mn

import coursesearch.Course
import coursesearch.Requirement

class CourseFulfillsRequirement implements Serializable {

    Course course

    Requirement requirement

    static mapping = {
        id(composite: ['course', 'requirement'])
        version(false)
    }
}
