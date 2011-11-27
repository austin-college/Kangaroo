import coursesearch.Course
import coursesearch.Professor
import coursesearch.Term
import grails.converters.JSON
import grails.util.Environment

class BootStrap {

    // Services to initialize our data.
    def backendDataService
    def courseImporterService
    def textbookDataService
    def cacheService

    def init = { servletContext ->

        println "\n\n==============================\n\n    Kangaroo v1 starting..."

        // Customize how objects are formatted to JSON.
        JSON.registerObjectMarshaller(Course) {
            return [id: it.id, name: it.name, items: it.items];
        }

        if (Environment.current != Environment.TEST) {
            backendDataService.upgradeAllIfNeeded()

            // Create terms and import courses.
            if (Term.count() == 0) {
                ["11FA", "12SP"].each {
                    def term = Term.findOrCreate(it)

                    if (Environment.current == Environment.DEVELOPMENT) {
                        println "Downloading course files..."
                        courseImporterService.importCourses(term)
                    }
                }

                textbookDataService.lookupTextbooksForAllCourses()
            }

//            cacheService.initializeCache()
        }

        println "\n==============================\n"
    }

    def destroy = {
    }
}
