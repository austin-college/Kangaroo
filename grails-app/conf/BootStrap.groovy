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

        // we have 3 types of users.
        if (AcRole.count() == 0) {
            ["ROLE_FACULTY", "ROLE_GUEST", "ROLE_ADMIN"].each { new AcRole(authority: it).save(flush: true) }
            println "${AcRole.count()} roles created."
        }

        // Create phil's local account for development since he isn't on LDAP.
        if (Environment.current == Environment.DEVELOPMENT && !AcUser.findByUsername("pcohen")) {
            println "Creating pcohen's account..."
            def phil = new AcUser(username: "pcohen", password: "pcohen").save();
            AcUserAcRole.create(phil, AcRole.findByAuthority("ROLE_ADMIN"));
            AcUserAcRole.create(phil, AcRole.findByAuthority("ROLE_FACULTY"));
            println "...done; ${AcUser.count()} users and ${AcUserAcRole.count()} user-roles"
        }

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
