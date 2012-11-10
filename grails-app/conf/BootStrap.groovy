import grails.converters.JSON
import grails.util.Environment
import kangaroo.api.EditKey
import kangaroo.*

class BootStrap {

    // Services to initialize our data.
    def backendDataService
    def courseImporterService
    def grailsApplication

    def init = { servletContext ->

        println "\n\n==============================\n\n    Kangaroo v${grailsApplication.metadata.'app.version'} starting..."

        registerJsonTypes()
        createDefaultData()

        println "\n==============================\n"
    }

    def destroy = {
    }

    private def createDefaultData() {

        // Fill in missing terms.
        ["11FA", "12SP", "12SU", "12FA", "13SP"].each { Term.findOrCreate(it) }

        // Create buildings.
        if (Building.count() == 0) {
            JSON.parse(new URL("https://raw.github.com/austin-college/Data/master/buildings.json").text).each { data ->
                def building = new Building();
                building.key = data.remove("id")
                building.properties = data;
                building.save()
            }
        }

        // Correct longitude and latitude.
        JSON.parse(new URL("http://pastebin.com/raw.php?i=VZ95TE9H").text).each { data ->
            def building = Building.findByNumberOnMap(data.numberOnMap) ?: Building.findByName(data.name);
            if (building) {
                building.longitude = new BigDecimal(data.longitude.toString());
                building.latitude = new BigDecimal(data.latitude.toString());
                building.save();
            }
            else
                println "Couldn't find building for ${data.name}..."
        }

        // Create user roles.
        if (AcRole.count() == 0) {
            ["ROLE_FACULTY", "ROLE_GUEST", "ROLE_ADMIN"].each { new AcRole(authority: it).save(flush: true) }
            println "${AcRole.count()} roles created."
        }

        // Create API key used to edit data.
        if (EditKey.count() == 0)
            new EditKey().save();

        // Create phil's local account for development since he isn't on LDAP.
        if (Environment.current == Environment.DEVELOPMENT && !AcUser.findByUsername("pcohen")) {
            println "Creating pcohen's account..."
            def phil = new AcUser(username: "pcohen", password: "pcohen").save();
            AcUserAcRole.create(phil, AcRole.findByAuthority("ROLE_ADMIN"));
            AcUserAcRole.create(phil, AcRole.findByAuthority("ROLE_FACULTY"));
            println "...done; ${AcUser.count()} users and ${AcUserAcRole.count()} user-roles"
        }

        if (Environment.current != Environment.TEST) {
            backendDataService.upgradeAllIfNeeded()

            // Import courses if we need to.
            if (Course.count() == 0) {

                println "Downloading course files..."
                Term.list().each { courseImporterService.importCourses(it) }
            }
        }
    }

    /**
     * Customize how objects are formatted to JSON by Grails.
     * It's silly that we have to do this via explicit commands.
     */
    private def registerJsonTypes() {

        JSON.registerObjectMarshaller(Building) { it.toJson() }
        JSON.registerObjectMarshaller(Course) { it.toJson() }
        JSON.registerObjectMarshaller(Department) { it.toJson() }
        JSON.registerObjectMarshaller(MeetingTime) { it.toJson() }
        JSON.registerObjectMarshaller(Professor) { it.toJson() }
        JSON.registerObjectMarshaller(Term) { it.toJson() }
    }
}
