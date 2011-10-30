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

            def events = [];

            professor.coursesTeaching.each { course ->
                course.meetingTimes.each { meetingTime ->
                    meetingTime.daysAsCodes.each { day ->

                        def date = getUpcomingWeekday(dayToOffset(day));

                        def startDate = setTime(date, 'hh:mma', meetingTime.startTime);
                        def endDate = setTime(date, 'hh:mma', meetingTime.endTime);

                        events << [title: course.name, allDay: false, start: startDate, end: endDate,
                                url: g.createLink(controller: "course", action: "show", id: course.id)];
                    }
                }
            }

            render(events as JSON);
        }
    }

    def getOfficeHours = {
        def professor = Professor.get(params.id as Long);

        if (professor) {

            def events = [];

            professor.officeHours.each { meetingTime ->
                meetingTime.daysAsCodes.each { day ->

                    def date = getUpcomingWeekday(dayToOffset(day));

                    def startDate = setTime(date, 'hh:mma', meetingTime.startTime);
                    def endDate = setTime(date, 'hh:mma', meetingTime.endTime);

                    events << [title: "Office Hours", allDay: false, start: startDate, end: endDate,
                            url: g.createLink(controller: "course", action: "show")];
                }
            }

            render(events as JSON);
        }
    }

    Date setTime(Date date, String format, String time) {

        def newDate = new Date().parse(format, time);

        newDate.setYear(date.year)
        newDate.setMonth(date.month)
        newDate.setDate(date.date)
        newDate
    }

    Date getUpcomingWeekday(int dayOfWeek) {
        def date = new Date();

        if (date[Calendar.DAY_OF_WEEK] != Calendar.MONDAY)
            date -= (date[Calendar.DAY_OF_WEEK] - Calendar.MONDAY)

        date += dayOfWeek;
    }

    int dayToOffset(code) {
        def days = ["M", "T", "W", "TH", "F"]
        return days.indexOf(code);
    }
}
