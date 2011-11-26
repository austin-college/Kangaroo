package coursesearch.data.prefill

import coursesearch.Department
import coursesearch.Major
import grails.converters.JSON
import org.springframework.transaction.annotation.Transactional

/**
 * Fills in data about Austin College's majors and minors.
 */
class MajorDataService {

    private static int lastVersionUsed = 0;

    @Transactional
    def upgradeIfNeeded() {

        def dataFromServer = getDataFromServer();

        if (dataFromServer.version > lastVersionUsed)
            upgradeMajors(dataFromServer)
        else
            println "Majors list is up to date; version $lastVersionUsed."
    }

    def upgradeMajors(dataFromServer) {

        println "Upgrading majors to version ${dataFromServer.version}..."

        // Remove the existing majors.
        Major.list().each { it.delete(flush: true) }

        // Add the new ones.
        dataFromServer.majors.each { importMajor(it) }

        lastVersionUsed = dataFromServer.version;
        println "Majors list is now at version ${lastVersionUsed}; ${Major.count()} majors saved."
    }

    def importMajor(data) {
        def department = Department.findByName(data.department);

        if (department) {

            def major = new Major(name: data.name, description: improveDescription(data.description, data.name), isMajor: data.isMajor, department: department);

            if (!major.save())
                println major.errors.toString()
        }
        else
            println "Department not found: ${data.department}"
    }

    def getDataFromServer() { JSON.parse(new URL("https://raw.github.com/austin-college/data/master/majors.json").text); }

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
