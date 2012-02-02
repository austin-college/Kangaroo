package coursesearch

import grails.converters.JSON
import coursesearch.data.convert.ScheduleProjectService
import coursesearch.data.convert.ScheduleConvertService

class ProfessorController {

    static def days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]

    def index = {
        redirect(controller: 'home')
    }

    def show = {
        def professor = Professor.get(params.id)

        if (professor)
            [professor: professor]
    }

    def setOfficeHours = {

        def professor = Professor.findByPrivateEditKey(params.id);
        if (professor)
            [professor: professor]
        else {
            flash.message = "Invalid edit key."
            redirect(controller: "home")
        }
    }

    def getStatus = {
        def professor = Professor.get(params.id)

        if (professor) {
            def status = getStatus(professor)
            render([html: g.render(template: "status", model: [status: status])] as JSON)
        }
    }

    def getStatus(Professor professor) {

        // First check the professor's office hour dates, to see if the professor is having office hours.
        for (def time: ScheduleProjectService.projectToWeek(professor.officeHours)) {
            if (isDateBetween(new Date(), time.startDate, time.endDate))
                return [status: "officeHours"]
        }

        // Next check the dates for each of the professor's courses, to see if the professor is in class.
        for (def course: professor.coursesTeaching.findAll { it.term == Term.findOrCreate("12SP")}) {
            for (def time: ScheduleProjectService.projectToWeek(course.meetingTimes)) {

                if (isDateBetween(new Date(), time.startDate, time.endDate))
                    return [status: "inClass", course: course]
            }
        }
    }

    boolean isDateBetween(Date toTest, Date start, Date end) {
        return (toTest > start) && (toTest < end);
    }

    /**
     * Returns a JSON list of this professor's class schedule for the week.
     */
    def getSchedule = {

        def professor = Professor.get(params.id);

        if (professor) {

            def events = []

            // For every course, convert its meeting times into real dates...
            professor.coursesTeaching.findAll { it.term == Term.findByShortCode("12SP") }.each { course ->
                ScheduleProjectService.projectToWeek(course.meetingTimes).each { time ->

                    // ...then add them to the calendar.
                    events << [title: course.name, allDay: false, start: time.startDate, end: time.endDate,
                            url: g.createLink(controller: "course", action: "show", id: course.id)]
                }
            }
            render(events as JSON);
        }
    }

    /**
     * Returns a JSON list of this professor's office hours for the week.
     */
    def getOfficeHours = {
        def professor = Professor.get(params.id);

        if (professor) {

            def events = ScheduleProjectService.projectToWeek(professor.officeHours).collect { time ->
                [title: "Office Hours", allDay: false, start: time.startDate, end: time.endDate]
            }

            render(events as JSON);
        }
    }
}
