import grails.converters.JSON
import grails.util.Environment
import kangaroo.Course
import kangaroo.Term

class BootStrap {

    // Services to initialize our data.
    def backendDataService
    def courseImporterService
    def textbookDataService
    def cacheService
    def grailsApplication

    def init = { servletContext ->

        println "\n\n==============================\n\n    Kangaroo v${grailsApplication.metadata.'app.version'} starting..."

        // Customize how objects are formatted to JSON.
        JSON.registerObjectMarshaller(Course) {
            return [id: it.id, name: it.name, items: it.items];
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
