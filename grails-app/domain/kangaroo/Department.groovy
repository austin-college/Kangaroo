package kangaroo

/**
 * Departments (Math, CS, Astrology) contain courses, majors, and professors.
 */
class Department {

    // The official code for this department (eg, "CS")
    String code

    // The full name of this department (eg, "Computer Science")
    String name

    static constraints = {
    }

    String toString() { name }
}
