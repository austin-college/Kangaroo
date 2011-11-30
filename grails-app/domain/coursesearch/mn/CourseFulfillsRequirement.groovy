package coursesearch.mn

import coursesearch.Course
import coursesearch.Requirement

/**
 * Many-many relationship between courses and requirements (course fulfills these requirements).
 */
class CourseFulfillsRequirement implements Serializable {

    static belongsTo = [requirement: Requirement]

    Course course

    static mapping = {
        id(composite: ['course', 'requirement'])
        version(false)
    }
}
