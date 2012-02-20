import grails.converters.JSON
import grails.util.Environment
import kangaroo.AppUtils
import kangaroo.Course
import kangaroo.Professor
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

        if (Environment.current != Environment.TEST) {
            //    backendDataService.upgradeAllIfNeeded()

            // Create terms and import courses.
            if (Term.count() == 0) {
                ["11FA", "12SP"].each {
                    def term = Term.findOrCreate(it)

                    println "Downloading course files..."
                    courseImporterService.importCourses(term)
                }

                textbookDataService.lookupTextbooksForAllCourses()
            }

//            cacheService.initializeCache()
        }

        // Give professors random edit tokens.
        Professor.findAllByPrivateEditKey(null).each {
            it.privateEditKey = AppUtils.generateRandomToken()
            it.save()
            println "${it} now has edit key ${it.privateEditKey}."
        }

        println "Setting names..."
        Professor.list().each { professor ->

            if (!professor.firstName)
                professor.firstName = professor.derivedFirstName
            if (!professor.lastName)
                professor.lastName = professor.derivedLastName

            if (professor.dirty) {
                professor.save(flush: true)
                println "$professor: $professor.errors"
            }
        }

        println "\n==============================\n"
    }

    def destroy = {
    }
}
