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
    def courseDataService
    def facultyDataService
    def textbookDataService
    def cacheService

    def init = { servletContext ->

        // Customize how objects are formatted to JSON.
        JSON.registerObjectMarshaller(Course) {
            return [id: it.id, name: it.name, items: it.items];
        }

        // Create terms.
        def terms = ["11FA", "12SP"]
        terms.each { Term.findOrCreate(it) }

        if (Environment.current != Environment.TEST) {
            departmentDataService.setUpDepartments()

            facultyDataService.fetchAndMatch()

            println "Reading in course files..."
            Term.list().each { term ->
                def filename = "courses_${term.shortCode}.json"
                if (new File(filename).exists())
                    courseImporterService.importFromJson(term, new File(filename).text);
                else
                    println "$filename does not exist"
            }

            // Pre-cache as much information as we can.
            cacheService.initializeCache();
        }
    }

    def destroy = {
    }
}
