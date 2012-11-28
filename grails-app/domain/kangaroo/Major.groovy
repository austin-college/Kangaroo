package kangaroo

/**
 * Represents a major or minor. The actual requirements for the major are currently not normalized (ie, a table for subrequirements), and are simply stored as HTML.
 * This is because they're so complicated -- approaching that problem will be quite an undertaking.
 */
class Major {

    // A safe, Outback-assigned key for URLs and JSON ("physicsMajor").
    String outbackId

    // The name of this major (often just the name of the department, i.e. "Mathematics").
    String name

    // Stores a description of the requirements for this major or minor (HTML).
    String description

    // Is this a major or a minor?
    // @todo Deprecate in the new schema for a "type" enum.
    boolean isMajor

    Department department

    static constraints = {
        outbackId(maxSize: 128, unique: true)
        name(maxSize: 128)
        description(maxSize: 8192)
    }

    String toString() { name }

    static Major saveFromJsonObject(object) {
        if (Major.findByOutbackId(object.id))
            return Major.findByOutbackId(object.id)

        def outbackId = object.id ?: AppUtils.camelCase(object.name + " " + object.type)
        return (Major) AppUtils.saveSafely(new Major(outbackId: outbackId, name: object.name, description: object.htmlDescription, department: Department.saveFromJsonObject(object.department), isMajor: (object.type == "major")));
    }

    def toJsonObject() { [id: outbackId, name: name, htmlDescription: description, department: department, type: isMajor ? "major" : "minor"] }
}
