package coursesearch.data.prefill

import coursesearch.Department
import coursesearch.Major
import grails.converters.JSON
import org.springframework.transaction.annotation.Transactional

/**
 * Fills in data about Austin College's majors and minors.
 */
class MajorDataService {

    @Transactional
    def setUpMajors() {

        // Remove the existing majors.
        Major.list().each { it.delete(flush: true) }

        majorsFromServer.each { data ->

            def department = Department.findByName(data.department);

            if (department) {

                def major = new Major(name: data.name, description: improveDescription(data.description), isMajor: data.isMajor, department: department);

                if (!major.save())
                    println major.errors.toString()
            }
            else
                println "Department not found: ${data.department}"
        }

        println "${Major.count()} majors saved successfully"
    }
    
    List getMajorsFromServer() { (List) JSON.parse(new URL("https://raw.github.com/austin-college/data/master/majors.json").text); }

    /**
     * Makes improvements to the HTML description of a major.
     */
    String improveDescription(String description, String majorName) {

        // Wrap the description in <p> tags if it isn't already.
        if (!description.startsWith("<p>") && !description.endsWith("</p>"))
            description = "<p>$description</p>";

        // Bold the "a major in xxx" part automatically.
        int startPosition = description.toLowerCase().indexOf("a ");
        int endPosition = description.toLowerCase().indexOf(majorName.toLowerCase()) + majorName.length();
        if (startPosition > -1 && endPosition > startPosition)
            description = description.substring(0, startPosition) + "<strong>" + description.substring(startPosition, endPosition) + "</strong>" + description.substring(endPosition);
        else
            println "'a' not found in $description"

        description;
    }
}
