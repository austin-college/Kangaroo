package coursesearch

class Teaching implements Serializable {

    Professor professor

    Course course

    static mapping = {
        id(composite: ['professor', 'course'])
        version(false)
    }
}
