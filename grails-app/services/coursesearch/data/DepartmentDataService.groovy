package coursesearch.data

import coursesearch.Department

/**
 * Fills the department with the list of departments.
 */
class DepartmentDataService {

    static transactional = true

    // Maps department codes to names.
    static def departmentMapping = [
            "ANTH": "Anthropology",
            "ART": "Art",
            "ARTH": "Art History",
            "ASST": "Asian Studies",
            "BA": "Business Administration",
            "BIOL": "Biology",
            "CHEM": "Chemistry",
            "CHIN": "Chinese",
            "CI": "C/I",
            "CL": "Communication/Leadership",
            "CLAS": "Classics",
            "COMM": "Communications",
            "CS": "Computer Science",
            "ECO": "Economics",
            "EDUC": "Education",
            "ENG": "English",
            "ENVS": "Environmental Studies",
            "ESS": "Exercise/Sports Sciences",
            "FR": "French",
            "GER": "German",
            "GNDR": "Gender Studies",
            "GRK": "Greek",
            "GS": "General Studies",
            "HIST": "History",
            "JAPN": "Japanese",
            "LAT": "Latin",
            "LEAD": "Leadership",
            "LS": "Life Sports",
            "MATH": "Mathematics",
            "MEDA": "Media",
            "ML": "Modern Language",
            "MUS": "Music",
            "PHIL": "Philosophy",
            "PHY": "Physics",
            "PSCI": "Political Science",
            "PSY": "Psychology",
            "REL": "Religious Studies",
            "SOC": "Sociology",
            "SPAN": "Spanish",
            "THEA": "Theater"
    ]

    /**
     * Ensures the department list is complete. Note, you call this even if you already have departments in the database; it will augment them.
     */
    def setUpDepartments() {

        departmentMapping.each { mapping ->
            def dept = Department.findByCode(mapping.key);
            if (dept) {
                if (dept.name != mapping.value) { // Update this department's name.
                    dept.name = mapping.value
                    dept.save();
                }
            }
            else
                new Department(code: mapping.key, name: mapping.value).save();
        }

    }
}
