package coursesearch.data.importer

import coursesearch.Course
import grails.converters.JSON
import coursesearch.Department
import coursesearch.Term
import coursesearch.Professor
import coursesearch.CourseUtils
import coursesearch.mn.Teaching
import org.springframework.transaction.annotation.Transactional
import coursesearch.MeetingTime
import coursesearch.data.convert.ScheduleConvertService
import coursesearch.mn.CourseMeetingTime
import org.hibernate.NonUniqueObjectException

/**
 * Imports course data from the new format -- a JSON dump created by WebhopperDriver.
 */
class CourseImporterService {

    def cacheService

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

        def meetingTimes = []
        data.schedules.each {
            def time = ScheduleConvertService.convertMeetingTime(it)?.saveOrFind()
            if (time)
                meetingTimes << new CourseMeetingTime(course: course, meetingTime: time)
        }
        def teachings = data.professors.collect { new Teaching(professor: findOrCreateProfessor(it.name, it.email), course: course) }

        if (!Course.get(course.id)) {
            if (course.save()) {
                meetingTimes.each { it.save(); }
                teachings.each { it.save(); }
            }
            else
                println course.errors
        }
        else
            println "There is already a course with the id ${course.id} (${Course.get(course.id)}"

    }
    //@Transactional
    Professor findOrCreateProfessor(name, email) {
        def professor = Professor.findByEmail(email)
        if (!professor)
            professor = new Professor(name: CourseUtils.cleanFacultyName(name), email: email).save()

        return professor
    }
}
