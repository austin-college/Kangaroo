package coursesearch

import grails.converters.JSON

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

    def getSchedule = {

        def professor = Professor.get(params.id as Long);

        if (professor) {

            def schedule = [:];

            // Get all of this professor's meeting times.
            professor.coursesTeaching.each { course ->
                course.meetingTimes.each { meetingTime ->
                    meetingTime.daysAsWords.each { day ->

                        if (!schedule[day])
                            schedule[day] = [];

                        schedule[day] << [course: course, time: meetingTime]
                    }
                }
            }

            def weekEvents = [[title:"Theory of Computation", allDay: false, start: new Date().parse('MM/dd/yyyy HH:mm', '10/24/2011 09:30'), end: new Date().parse('MM/dd/yyyy HH:mm', '10/24/2011 10:50'),
             url:g.createLink(controller:"course", action:"show", id: 40217)],
            [id:111, title:"Event1", start:2011-10-26, url:"http://yahoo.com/"]]//, ]]

            render(weekEvents as JSON);
        }
    }
}
