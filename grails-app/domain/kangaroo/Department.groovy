package kangaroo

/**
 * Departments (Math, CS, Astrology) contain courses, majors, and professors.
 */
class Department {

    // The official code for this department (eg, "CS")
    String id

    // The full name of this department (eg, "Computer Science")
    String name = id

    static constraints = {
        id(maxSize: 8)
        name(maxSize: 64)
    }

    static mapping = {
        id(column: 'code', generator: 'assigned')
    }

    String toString() { name }

    def toJson() { [id: this.id, name: this.name] }
}
