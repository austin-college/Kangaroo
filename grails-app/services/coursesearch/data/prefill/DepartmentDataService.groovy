package coursesearch.data.prefill

import coursesearch.Department
import coursesearch.CourseUtils
import groovy.util.slurpersupport.GPathResult

/**
 * Fills the department with the list of departments.
 */
class DepartmentDataService {

    static transactional = true

    // Maps department codes to names.
    static def departmentMapping = [:]

    // Store the html from WebHopper's "Department" <select> here (@todo export).
    static final def html = """<select name="LIST.VAR1_1" id="LIST_VAR1_1"><option value=""></option><option value="AFST">African Studies</option><option value="AMST">American Studies</option><option value="ANTH">Anthropology</option><option value="ART">Art</option><option value="ARTH">Art History</option><option value="ASST">Asian Studies</option><option value="BA">Business Admin</option><option value="BIOL">Biology</option><option value="CHEM">Chemistry</option><option value="CHIN">Chinese</option><option value="CI">Commun/Inquiry</option><option value="CL">Commun/Leadership</option><option value="CLAS">Classics</option><option value="COGS">Cognitive Science</option><option value="COMM">Communication</option><option value="COMS">Communication Studies</option><option value="CS">Computer Science</option><option value="CSP">Community Service &amp; Policy</option><option value="ECO">Economics</option><option value="EDUC">Education</option><option value="ENG">English</option><option value="ENVS">Environmental Stds</option><option value="ESS">Exercise &amp; Sports Sc</option><option value="FILM">Film Studies</option><option value="FR">French</option><option value="GER">German</option><option value="GNDR">Gender Studies</option><option value="GRK">Greek</option><option value="GS">General Studies</option><option value="GSTS">Global Sci, Technol, &amp; Society</option><option value="HIST">History</option><option value="HUM">Humanities</option><option value="JAPN">Japanese</option><option value="LAST">Latin Amer Studies</option><option value="LAT">Latin</option><option value="LEAD">Leadership</option><option value="LS">Lifetime Sports</option><option value="MATH">Mathematics</option><option value="MEDA">Media</option><option value="ML">Modern Language</option><option value="MUS">Music</option><option value="PHIL">Philosophy</option><option value="PHY">Physics</option><option value="PSCI">Political Science</option><option value="PSY">Psychology</option><option value="REL">Religion</option><option value="SCI">Science</option><option value="SOC">Sociology</option><option value="SPAN">Spanish</option><option value="SPCH">Speech</option><option value="SSCI">Social Science</option><option value="THEA">Theatre</option><option value="WIT">Western Intellectual Tradition</option></select>"""

    /**
     * Ensures the department list is complete. Note, you call this even if you already have departments in the database; it will augment them.
     */
    def setUpDepartments() {

        getMappings(html);

        // Create departments for the mappings.
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

    /**
     * Extracts all the departments from the given html snippet and stores it in departmentMapping.
     */
    def getMappings(String html) {

        def node = CourseUtils.cleanAndConvertToXml(html);

        CourseUtils.findAllInNode(node) { it.name() == "option" }.each {

            // Process each <option> and store its data.
            def info = getDepartmentFromOption(it)
            if (info)
                departmentMapping[info.code] = info.name;
        }
    }

    /**
     * Given a <option> node from the department <select> box, returns the department's code and name.
     * example:  [code: "ANTH", name: "Anthropology"]
     */
    Map getDepartmentFromOption(GPathResult option) {
        if (option?.toString())
            [code: option.@value.toString(), name: option.toString()]
    }
}
