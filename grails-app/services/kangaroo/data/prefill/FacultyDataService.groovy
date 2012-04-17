package kangaroo.data.prefill

import kangaroo.Professor
import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * Downloads the faculty page from GitHub.
 */
class FacultyDataService extends UpdateableDataService {

    static transactional = true
    static String name = "Faculty list"
    static String url = "${dataRoot}/newFaculty.json"
    static int lastVersionUsed = 0;

    def cacheService

    @Override
    protected void upgradeAll(dataFromServer) {

        // Add the new professors.
        def itemsToKeep = []
        dataFromServer.professors.each { id, details ->

            def professor = Professor.get(id)
            if (!professor)
                professor = new Professor(id: id)

            map(professor, details)
            itemsToKeep << professor.save()
        }

//        // Remove professors that no longer appear in the list.
//        (Department.list() - itemsToKeep).each { toDelete ->
//            if (Course.countByDepartment(toDelete) == 0)
//                toDelete.delete(flush: true)
//        }

        // This modifies the course table.
        cacheService.initializeCache()
    }

    protected void map(Professor professor, Map details) {
        professor.firstName = checkForNull(details.firstName)
        professor.middleName = checkForNull(details.middleName)
        professor.lastName = checkForNull(details.lastName)
        professor.photoUrl = checkForNull(details.photoUrl)
        professor.title = checkForNull(details.title)
        professor.department = checkForNull(details.departmentGroup)
        professor.office = checkForNull(details.office)
        professor.phone = checkForNull(details.phone)
        professor.email = checkForNull(details.email)
    }

    String checkForNull(value) {
        return (value.equals(null) ? null : value) // Necessary because JSON nulls are actually parsed as JSONObject.Null; they equals() null but are not null.
    }
}
