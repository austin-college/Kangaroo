package coursesearch.data.prefill

import coursesearch.Course
import coursesearch.Department

/**
 * Fills the department with the list of departments.
 */
class DepartmentDataService extends UpdateableDataService {

    static String name = "Departments list"
    static String url = "${dataRoot}/departments.json"
    static int lastVersionUsed = 0;

    def cacheService

    @Override
    protected void upgradeAll(dataFromServer) {

        // Add the new departments.
        def itemsToKeep = []
        dataFromServer.list.each { data ->

            def existing = Department.findByCode(data.code)
            if (existing) {
                existing.code = data.code;
                existing.name = data.name;
                itemsToKeep << existing.save();
            }
            else
                itemsToKeep << new Department(code: data.code, name: data.name).save();
        }

        // Remove departments that no longer appear in the list.
        // NOTE / @todo: Departments cannot be removed if they have courses pointing to them.
        (Department.list() - itemsToKeep).each { toDelete ->
            if (Course.countByDepartment(toDelete) == 0)
                toDelete.delete(flush: true)
        }

        // This modifies the course table.
        cacheService.clearCache()
    }
}
