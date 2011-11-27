package coursesearch.mn

import coursesearch.Course
import coursesearch.Requirement

class CourseFulfillsRequirement implements Serializable {

    static belongsTo = [requirement: Requirement]

    Course course

    static mapping = {
        id(composite: ['course', 'requirement'])
        version(false)
    }
}
