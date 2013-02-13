package kangaroo.setup.fetch

import grails.converters.JSON
import kangaroo.*
import kangaroo.data.convert.ScheduleConvertService
import kangaroo.mn.CourseFulfillsRequirement
import kangaroo.mn.CourseMeetingTime
import kangaroo.mn.Teaching

/**
 * Imports course data from the new format -- a JSON dump created by WebhopperDriver.
 */
class CourseImporterService {

    final static placeholderProfessorNames = ["STAFF", "No Information Available"]

    static String lastDepartment

    def cacheService

    def importCourses(Term term) {
        importFromJson(term, new URL("https://raw.github.com/austin-college/Data/master/courses/${term.id}.json").text)
    }

    def deleteCourses(Term term) {

        println "Deleting ALL courses for term $term..."
        Course.findAllByTerm(term).each { deleteSingleCourse(it) }
        cacheService.clearCache();
    }

    def deleteSingleCourse(Course course) {

        println "Deleting $course..."
        CourseFulfillsRequirement.findAllByCourse(course).each { it.delete() }
        CourseMeetingTime.findAllByCourse(course).each { it.delete() }
        Teaching.findAllByCourse(course).each { it.delete() }
        course.delete(flush: true);
        if (course.hasErrors())
            println "ERROR DELETING: " + course.errors.toString()
    }

//    @Transactional
    def importFromJson(Term term, String json) {

        // First delete all the old courses!
        deleteCourses(term);

        println "\n==== IMPORTING FOR $term ====\n"
        def courses = JSON.parse(json)
        courses.each { saveSingleCourse(term, it) }

        // Now match courses to labs...
        println "Matching labs..."
        Course.findAllWhere([term: term, isLab: true]).each { lab ->
            String courseName;

            // All of its siblings must have labs, too.
            Course.findAllSections(term, lab.department, lab.courseNumber).findAll { !it.isLab }.each { course ->
                course.hasLabs = true;
                courseName = course.name;
                course.save();
            }

            // Improve the name presentation.
            lab.name = "Lab: ${courseName}"
            lab.save();
        }

        // Naturally we'll want to clear the cache.
        cacheService.clearCache()

        println "\n=== DONE IMPORTING $term ===\n"
    }

//    @Transactional
    def saveSingleCourse(Term term, Map data) {
//        println "Processing ${data['name']}..."
        def description = data.remove("description")?.replaceAll("Formerly", "<br/>Formerly")
        Course course = (data as Course)
        course.term = term

        // Convert zap, department, and description.
        course.zap = data.zap
        course.description = BigText.getOrCreate(description);
        course.department = Department.get(data.departmentCode);
        if (!course.department) {
            course.department = new Department(id: data.departmentCode, name: data.departmentCode);
            course.department.id = data.departmentCode // For manually assigned IDs you can't do this in the constructor or you get a Hibernate error
            course.department.save();
        }

        course.department.merge(flush: true);

        if (course.department.toString() != lastDepartment) {
            println "Now loading " + course.department.toString() + " (" + data.departmentCode + ") ...";
            lastDepartment = course.department.toString();
        }

        if (course.isLab && course.section == 'A') {
            println "Note: ${course.department} ${course.courseNumber}${course.section} is a lab but not correctly labelled. Fixing..."
            course.section = 'L';
        }

        course.id = course.generateIdString()
        def courseRequirements = getRequirements(data.reqCode).collect { new CourseFulfillsRequirement(course: course, requirement: it) }
        def meetingTimes = []
        Set<String> schedules = new HashSet<String>(data.schedules);
        schedules.each { it ->
            def time = ScheduleConvertService.convertMeetingTime(it)?.saveOrFind()
            if (time)
                meetingTimes << new CourseMeetingTime(course: course, meetingTime: time)
        }
        def teachings = []
        data.professors.each {
            def prof = findOrCreateProfessor(it.name, it.email)
            if (prof)
                teachings << new Teaching(professor: prof, course: course)
//            else
//                println "NO PROFESSOR FOR \"${course.name}\": \"${it.name}\" given as a name."
        }

        if (!Course.get(course.id)) {
            if (course.validate()) {
                course.save(flush: true)
                meetingTimes.each { it.save(); }
                teachings.each { it.save(); }
                courseRequirements.each { it.save(); }
            } else
                println course.errors
        } else
            println "There is already a course with the id ${course.id} (${Course.get(course.id)})" // Shouldn't happen anymore with the new IDs

    }

    //@Transactional
    Professor findOrCreateProfessor(name, email) {

        // Check if this isn't a real professor.
        for (String toAvoid : placeholderProfessorNames)
            if (name.equals(toAvoid))
                return null;

        def id = AppUtils.extractProfessorUsername(email, name)
        def professor = Professor.get(id)
        if (!professor && id) {
            professor = new Professor(email: email)
            professor.id = id;
            def splitName = AppUtils.cleanFacultyName(name);
            professor.firstName = splitName.firstName;
            professor.lastName = splitName.lastName;

            if (!email) {
//                professor.id = AppUtils.extractProfessorUsernameFromName(name)
                println "NO EMAIL ====> " + name + "//" + professor.id
            }
            professor = professor.save()
        }

        return professor
    }

    List<Requirement> getRequirements(String reqCode) {
        reqCode.split(" ").collect { Requirement.get(it) }
    }
}
