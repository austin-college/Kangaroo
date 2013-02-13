package kangaroo

import kangaroo.mn.CourseFulfillsRequirement

/**
 * Represents a graduation requirement code (i.e. "HU" - "Humanities").
 *
 * Students need x number of courses in each distribution to graduate (@todo put these details into Kangaroo).
 * Individual courses are listed as fulfilling some requirements.
 */
class Requirement {

    String id // The code ("HU", "SS")
    String name

    // True if this isn't a "requirement" per se, but a marker for courses that satisfy inter-disciplinary majors (Environmental Studies, Asian Studies, Film Studies, etc.)
    // These majors get requirement codes because their courses appear across many departments, and otherwise it's hard to search for them.
    // @todo refactor to "type" enum
    boolean isInterdisciplinaryMajor

    static constraints = {
        id(maxSize: 8)
        name(maxSize: 96)
    }

    static mapping = {
        id(column: 'code', generator: 'assigned')
    }

    List<Course> getCoursesThatFulfill() { CourseFulfillsRequirement.findAllByRequirement(this)*.course; }

    String toString() { name }

    static Requirement saveFromJsonObject(object) {
        if (Requirement.get(object.id))
            return Requirement.get(object.id);

        def requirement = new Requirement(name: object.name, isInterdisciplinaryMajor: (object.type == "interdisciplinaryMajor"))
        requirement.id = object.id;
        return (Requirement) AppUtils.saveSafely(requirement);
    }

    def toJsonObject() { [id: id, name: name, type: isInterdisciplinaryMajor ? "interdisciplinaryMajor" : "requirement"]; }

}
