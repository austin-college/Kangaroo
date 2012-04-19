import grails.converters.JSON
import grails.util.Environment
import kangaroo.Course
import kangaroo.Department
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
        JSON.registerObjectMarshaller(Course) { Course course ->
            [id: course.id, name: course.name, description: course.description?.description, zap: course.zap, open: course.open,
                    capacity: course.capacity, isLab: course.isLab, hasLabs: course.hasLabs, instructorConsentRequired: course.instructorConsentRequired,
                    department: course.department, courseNumber: course.courseNumber, section: course.section.toString(), room: course.room, meetingTimes: course.meetingTimes*.toString(),
                    comments: course.comments
            ];
        }
        JSON.registerObjectMarshaller(Department) {
            [id: it.id, name: it.name]
        }
        JSON.registerObjectMarshaller(Professor) {
            return [id: it.id, firstName: it.firstName, middleName: it.lastName, lastName: it.lastName, title: it.title,
                    departmentGroup: it.department, email: it.email, office: it.office, phone: it.phone, photoURL: it.photoUrl];
        }
        JSON.registerObjectMarshaller(Term) {
            return [id: it.id, description: it.fullDescription, year: it.year, season: it.season, courses: Course.findAllByTerm(it).collectEntries {[it.id, it]}];
        }

        // Create terms if we need to.
        if (Term.count() == 0)
            ["11FA", "12SP", "12SU", "12FA"].each { Term.findOrCreate(it) }

        // Make everyone a professor.
        Professor.list().each { it.isProfessor = true; it.save() }

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
