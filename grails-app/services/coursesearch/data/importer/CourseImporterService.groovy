package coursesearch.data.importer

import coursesearch.Course
import grails.converters.JSON
import coursesearch.Department
import coursesearch.Term
import coursesearch.Professor
import coursesearch.CourseUtils
import coursesearch.mn.Teaching
import org.springframework.transaction.annotation.Transactional

/**
 * Imports course data from the new format -- a JSON dump created by WebhopperDriver.
 */
class CourseImporterService {

    def cacheService

    @Transactional
    def importFromJson(String json) {
        def courses = JSON.parse(json)
        courses.each { saveSingleCourse(it)}

        // Naturally we'll want to clear the cache.
        cacheService.clearCache()
        cacheService.initializeCache()
    }

    @Transactional
    def saveSingleCourse(Map data) {
        Course course = (data as Course)

        // @todo receive this as a variable
        course.term = Term.findOrCreate("11SP")

        // Convert zap, department, and description.
        course.id = data.zap
        course.description = data.details
        course.department = Department.findByCode(data.departmentCode) ?: new Department(code: data.departmentCode, name: data.departmentCode).save();

        def teachings = data.professors.collect { new Teaching(professor: findOrCreateProfessor(it.name, it.email), course: course) }

        if (course.save())
            teachings.each { it.save(); }
        else
            println course.errors
    }

    @Transactional
    Professor findOrCreateProfessor(name, email) {
        def professor = Professor.findByEmail(email)
        if (!professor)
            professor = new Professor(name: CourseUtils.cleanFacultyName(name), email: email).save()

        return professor
    }
}
