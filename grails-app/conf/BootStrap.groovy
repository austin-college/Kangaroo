import grails.converters.JSON
import grails.util.Environment
import kangaroo.Course
import kangaroo.Professor
import kangaroo.Term
import kangaroo.api.EditKey

class BootStrap {

    // Services to initialize our data.
    def backendDataService
    def courseImporterService
    def textbookDataService
    def cacheService
    def grailsApplication

    def init = { servletContext ->

        println "\n\n==============================\n\n    Kangaroo v${grailsApplication.metadata.'app.version'} starting..."

        if (EditKey.count() == 0)
            new EditKey().save();

        // Customize how objects are formatted to JSON.
        JSON.registerObjectMarshaller(Course) {
            return [id: it.id, name: it.name, items: it.items];
        }
        JSON.registerObjectMarshaller(Professor) {
            return [id: it.id, firstName: it.firstName, middleName: it.lastName, lastName: it.lastName, title: it.title,
                    departmentGroup: it.department, email: it.email, office: it.office, phone: it.phone, photoURL: it.photoUrl];
        }

        // Create terms if we need to.
        if (Term.count() == 0)
            ["11FA", "12SP", "12SU", "12FA"].each { Term.findOrCreate(it) }

        if (Environment.current != Environment.TEST) {
            backendDataService.upgradeAllIfNeeded()

            // Import courses if we need to.
            if (Course.count() == 0) {

                println "Downloading course files..."
                Term.list().each { courseImporterService.importCourses(it) }
            }

            cacheService.initializeCacheIfNeeded()
        }

        println "\n==============================\n"
    }

    def destroy = {
    }
}
