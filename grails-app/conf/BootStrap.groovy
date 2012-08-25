import grails.converters.JSON
import grails.util.Environment
import kangaroo.api.EditKey
import kangaroo.*

class BootStrap {

    // Services to initialize our data.
    def backendDataService
    def courseImporterService
    def textbookDataService
    def cacheService
    def grailsApplication

    def init = { servletContext ->

        println "\n\n==============================\n\n    Kangaroo v${grailsApplication.metadata.'app.version'} starting..."

        // Create the default API edit key if none exist.
        if (EditKey.count() == 0)
            new EditKey().save();

        // Create the default roles if none exist.
        Role.findOrSaveWhere(authority: "ROLE_ADMIN");

        // Create the default admin user if none exist.
        if (User.count() == 0) {
            def admin = new User(username: "admin", password: "admin");
            admin.save();
            UserRole.create(admin, Role.findByAuthority("ROLE_ADMIN"));
        }

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
        JSON.registerObjectMarshaller(MeetingTime) {
            it.toString()
        }
        JSON.registerObjectMarshaller(Professor) { Professor it ->
            return [id: it.id, firstName: it.firstName, middleName: it.lastName, lastName: it.lastName, title: it.title,
                    departmentGroup: it.department, email: it.email, office: it.office, phone: it.phone, photoURL: it.photoUrl,
                    officeHours: it.officeHours];
        }
        JSON.registerObjectMarshaller(Term) {
            return [id: it.id, description: it.fullDescription, year: it.year, season: it.season, isActive: it.id == Term.CURRENT_TERM_CODE];
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
        }

        println "\n==============================\n"
    }

    def destroy = {
    }
}
