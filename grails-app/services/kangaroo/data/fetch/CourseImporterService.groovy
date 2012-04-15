package kangaroo.data.fetch

import grails.converters.JSON
import kangaroo.data.convert.ScheduleConvertService
import kangaroo.mn.CourseFulfillsRequirement
import kangaroo.mn.CourseMeetingTime
import kangaroo.mn.Teaching
import kangaroo.*

/**
 * Imports course data from the new format -- a JSON dump created by WebhopperDriver.
 */
class CourseImporterService {

    final static placeholderProfessorNames = ["STAFF", "No Information Available"]

    def cacheService

    def importCourses(Term term) {
        importFromJson(term, new URL("https://raw.github.com/austin-college/Data/master/courses/${term.id}.json").text)
    }

//    @Transactional
    def importFromJson(Term term, String json) {
        def courses = JSON.parse(json)
        courses.each { saveSingleCourse(term, it)}

        // Now find which courses are labs of other courses.
        println "Matching labs..."
        Course.findAllWhere([term: term, isLab: true]).each { course ->
            def pattern = /\*\* ([A-Z]+)\*(\d+) Lab/;
            if (course.name =~ pattern) {
                def matchedLab = ( course.name =~ pattern );
                course.labOf = Course.findWhere([term: term, isLab: false, department: Department.get(matchedLab[0][1]), courseNumber: Integer.parseInt(matchedLab[0][2])])
                course.save();
            }
        }

        // Naturally we'll want to clear the cache.
        cacheService.clearCache()
        cacheService.initializeCache()
    }

//    @Transactional
    def saveSingleCourse(Term term, Map data) {
        def description = data.remove("description")?.replaceAll("Formerly", "<br/>Formerly")
        Course course = (data as Course)
        course.term = term

        // Convert zap, department, and description.
        course.zap = data.zap
        course.description = BigText.getOrCreate(description);
        course.department = Department.findOrSaveWhere(id: data.departmentCode);

        if (course.isLab && course.section == 'A') {
            println "Note: ${course.department} ${course.courseNumber}${course.section} is a lab but not correctly labelled. Fixing..."
            course.section = 'L';
        }

        course.id = course.generateIdString()
        def courseRequirements = getRequirements(data.reqCode).collect { new CourseFulfillsRequirement(course: course, requirement: it) }
        def meetingTimes = []
        data.schedules.each {
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
            if (course.save()) {
                meetingTimes.each { it.save(); }
                teachings.each { it.save(); }
                courseRequirements.each { it.save(); }
            }
            else
                println course.errors
        }
        else
            println "There is already a course with the id ${course.id} (${Course.get(course.id)})"

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
