package coursesearch.data.prefill

import coursesearch.Course
import coursesearch.Requirement
import coursesearch.mn.CourseFulfillsRequirement
import org.springframework.transaction.annotation.Transactional

class RequirementsDataService extends UpdateableDataService {

    static String name = "Requirements list"
    static String url = "${dataRoot}/requirements.json"

    @Override
    protected void upgradeAll(dataFromServer) {

        // Save the mapping of courses to requirements.
        def mappingCache = [:]
        CourseFulfillsRequirement.list().each { mapping ->
            if (!mappingCache[mapping.course])
                mappingCache[mapping.course] = [];

            mappingCache[mapping.course] << mapping.requirement.code;
        }

        // Remove the existing majors and mappings.
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