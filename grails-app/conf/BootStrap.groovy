import coursesearch.Course
import coursesearch.Textbook
import grails.converters.JSON
import grails.util.Environment
import coursesearch.Term
import coursesearch.Professor

class BootStrap {

    // Services to initialize our data.
    def departmentDataService
    def courseImporterService
    def facultyDataService
    def textbookDataService
    def cacheService

    def init = { servletContext ->

        // Customize how objects are formatted to JSON.
        JSON.registerObjectMarshaller(Course) {
            return [id: it.id, name: it.name, items: it.items];
        }

        if (Environment.current != Environment.TEST) {
            departmentDataService.setUpDepartments()

            facultyDataService.fetchAndMatch()

            // Create terms and import courses.
            println "Downloading course files..."
            ["11FA", "12SP"].each {
                def term = Term.findOrCreate(it)
                courseImporterService.importFromJson(term, new URL("http://phillipcohen.net/accourses/courses_${term.shortCode}.json").text)
            }

            textbookDataService.lookupTextbooksForAllCourses()
        }
    }

    def destroy = {
    }
}
