package coursesearch.mn

import coursesearch.Professor
import coursesearch.Course

class Teaching implements Serializable {

    Professor professor

    Course course

    static mapping = {
        id(composite: ['professor', 'course'])
        version(false)
    }
}
