import coursesearch.Course
import coursesearch.Professor
import grails.converters.JSON

class BootStrap {

    // Services to initialize our data.
    def departmentDataService
    def courseDataService
    def facultyDataService
    def cacheService

    def init = { servletContext ->

        // Customize how objects are formatted to JSON.
        JSON.registerObjectMarshaller(Course) {
            return [id: it.id, name: it.name, items: it.items];
        }

        departmentDataService.setUpDepartments()
        if (Course.count() == 0)
            courseDataService.downloadAndProcess();
        facultyDataService.fetchAndMatch()

        // Pre-cache as much information as we can.
        cacheService.initializeCache();
    }

    def destroy = {
    }
}
