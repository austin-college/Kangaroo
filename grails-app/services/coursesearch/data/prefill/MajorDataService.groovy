package coursesearch.data.prefill

import coursesearch.Department
import coursesearch.Major
import grails.converters.JSON

class MajorDataService {

    static transactional = true

    def setUpMajors() {

        // Remove the existing majors.
        Major.list().each { it.delete(flush: true) }

        listFromServer.each { data ->

            def department = Department.findByName(data.department);

            if (department) {

                String description = data.description;

                // Wrap the description in <p> tags if it isn't already.
                if (!description.startsWith("<p>") && !description.endsWith("</p>"))
                    description = "<p>$description</p>";

                // Bold the "a major in xxx" part automatically.
                int startPosition = description.toLowerCase().indexOf("a ");
                int endPosition = description.toLowerCase().indexOf(data.name.toString().toLowerCase());
                if (startPosition > -1 && endPosition > startPosition)
                    description = description.substring(0, startPosition) + "<strong>" + description.substring(startPosition, endPosition + data.name.toString().length()) + "</strong>" + description.substring(endPosition + data.name.toString().length());
                else
                    println "'a' not found in $description"

                def major = new Major(name: data.name, description: description,
                        isMajor: data.isMajor, department: department);

                if (!major.save())
                    println major.errors.toString()
            }
            else
                println "Department not found: ${data.department}"
        }

        println "${Major.count()} majors saved successfully"
    }
    
    List getListFromServer() { (List) JSON.parse(new URL("https://raw.github.com/austin-college/data/master/majors.json").text); }
}
