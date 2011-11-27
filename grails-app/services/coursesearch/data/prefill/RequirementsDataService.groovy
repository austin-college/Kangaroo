package coursesearch.data.prefill

import coursesearch.Course
import coursesearch.CourseUtils
import coursesearch.Requirement
import coursesearch.mn.CourseFulfillsRequirement
import grails.converters.JSON
import org.springframework.transaction.annotation.Transactional

class RequirementsDataService {

    private static int lastVersionUsed = 0;

    @Transactional
    def upgradeIfNeeded() {

        def dataFromServer = getDataFromServer();

        if (dataFromServer.version > lastVersionUsed)
            upgradeAll(dataFromServer)
        else
            println "Requirements list is up to date; version $lastVersionUsed."
    }

    def upgradeAll(dataFromServer) {

        println "Upgrading requirements to version ${dataFromServer.version}..."

        CourseUtils.runAndTime("Requirements list updated to version ${lastVersionUsed}") {

            // Save the mapping of courses to requirements.
            def mappingCache = [:]
            CourseFulfillsRequirement.list().each { mapping ->
                if (!mappingCache[mapping.course])
                    mappingCache[mapping.course] = [];

                mappingCache[mapping.course] << mapping.requirement.code;
            }

            println "Mapping cache is: $mappingCache"

            // Remove the existing majors.
            Requirement.list().each { existing ->
                CourseFulfillsRequirement.findAllByRequirement(existing).each { it.delete(flush: true) }
                existing.delete(flush: true)
            }

            // Add the new requirements.
            dataFromServer.list.each { data -> new Requirement(code: data.code, name: data.name, isInterdisciplinaryMajor: data.isDiscipline).save(); }

            // Recreate the mappings.
            mappingCache.each { Course course, List<String> codes ->
                codes.each { code ->
                    new CourseFulfillsRequirement(course: course, requirement: Requirement.findByCode(code)).save();
                }
            }

            lastVersionUsed = dataFromServer.version;
        }
    }

    def getDataFromServer() { JSON.parse(new URL("https://raw.github.com/austin-college/data/master/requirements.json").text); }
}