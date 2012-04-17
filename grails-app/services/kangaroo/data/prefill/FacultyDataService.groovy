package kangaroo.data.prefill

import kangaroo.Professor

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
        professor.firstName = details.firstName;
        professor.middleName = details.middleName;
        professor.lastName = details.lastName;
        professor.photoUrl = details.photoUrl;
        professor.title = details.title;
        professor.department = details.departmentGroup;
        professor.office = details.office
        professor.phone = details.phone;
        professor.email = details.email;
    }
}
