package kangaroo

/**
 * Represents a major or minor. The actual requirements for the major are currently not normalized (ie, a table for subrequirements), and are simply stored as HTML.
 * This is because they're so complicated -- approaching that problem will be quite an undertaking.
 */
class Major {

    // The name of this major (often just the name of the department, i.e. "Mathematics").
    String name

    // Stores a description of the requirements for this major or minor (HTML).
    String description

    // Is this a major or a minor?
    // @todo Deprecate in the new schema for a "type" enum.
    boolean isMajor

    Department department

    static constraints = {
        name(maxSize: 128)
        description(maxSize: 8192)
    }

    String toString() { name }

    static Major fromJsonObject(object) {
        return (Major) AppUtils.saveSafely(new Major(name: object.name, description: object.description, department: Department.fromJsonObject(object.department), isMajor: (object.type == "major")));
    }

    def toJsonObject() { [name: name, description: description, department: department, type: isMajor ? "major" : "minor"] }
}
