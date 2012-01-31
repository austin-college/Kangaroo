package coursesearch.data.fetch

import coursesearch.data.convert.ScheduleConvertService
import coursesearch.mn.CourseFulfillsRequirement
import coursesearch.mn.CourseMeetingTime
import coursesearch.mn.Teaching
import grails.converters.JSON
import coursesearch.*

/**
 * Imports course data from the new format -- a JSON dump created by WebhopperDriver.
 */
class CourseImporterService {

    def cacheService

    def importCourses(Term term) {
        importFromJson(term, new URL("https://raw.github.com/austin-college/data/master/courses/${term.shortCode}.json").text)
    }

//    @Transactional
    def importFromJson(Term term, String json) {
        def courses = JSON.parse(json)
        courses.each { saveSingleCourse(term, it)}

        // Naturally we'll want to clear the cache.
        cacheService.clearCache()
        cacheService.initializeCache()
    }

//    @Transactional
    def saveSingleCourse(Term term, Map data) {
        Course course = (data as Course)
        course.term = term

        // Convert zap, department, and description.
        course.id = data.zap
        course.description = course.description?.replaceAll("Formerly", "<br/>Formerly");
        course.department = Department.findByCode(data.departmentCode) ?: new Department(code: data.departmentCode, name: data.departmentCode).save();

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
            else
                println "NO PROFESSOR FOR \"${course.name}\": \"${it.name}\" given as a name."
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
            println "There is already a course with the id ${course.id} (${Course.get(course.id)}"

    }
    //@Transactional
    Professor findOrCreateProfessor(name, email) {
        def id = CourseUtils.extractProfessorUsername(email, name)
        def professor = Professor.get(id)
        if (!professor && id) {
            professor = new Professor(name: CourseUtils.cleanFacultyName(name), email: email)
            professor.id = id;
            if (!email) {
//                professor.id = CourseUtils.extractProfessorUsernameFromName(name)
                println "NO EMAIL ====> " + name + "//" + professor.id
            }
            professor = professor.save()
        }

        return professor
    }

    List<Requirement> getRequirements(String reqCode) {
        reqCode.split(" ").collect { Requirement.findByCode(it) }
    }
}
