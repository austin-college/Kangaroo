package coursesearch

class Major {

    String name
    String description
    boolean isMajor
    Department department

    static constraints = {
        name(maxSize: 128)
        description(maxSize: 8192)
    }
}
