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

        if (professor) {
            [professor: professor]
        }
    }

    /**
     * Returns a JSON list of this professor's class schedule for the week.
     */
    def getSchedule = {

        def professor = Professor.get(params.id as Long);

        if (professor) {

            def events = []

            // For every course, convert its meeting times into real dates...
            professor.coursesTeaching.each { course ->
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
        def professor = Professor.get(params.id as Long);

        if (professor) {

            def events = ScheduleProjectService.projectToWeek(professor.officeHours).collect { time ->
                [title: "Office Hours", allDay: false, start: time.startDate, end: time.endDate]
            }

            render(events as JSON);
        }
    }
}
