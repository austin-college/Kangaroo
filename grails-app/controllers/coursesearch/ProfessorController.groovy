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

    /**
     * Returns a JSON list of this professor's class schedule for the week.
     */
    def getSchedule = {

        def professor = Professor.get(params.id as Long);

        if (professor) {

            def events = []

            professor.coursesTeaching.each { course ->
                projectToWeek(course.meetingTimes).each { time ->
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

            def events = [];

            projectToWeek(professor.officeHours).each { time ->
                events << [title: "Office Hours", allDay: false, start: time.startDate, end: time.endDate]
            }

            render(events as JSON);
        }
    }

    /**
     * Given a list of metingTimes, returns a list of startTimes and endTimes for this week.
     */
    def projectToWeek(meetingTimes) {
        def events = []
        meetingTimes.each { meetingTime ->
            meetingTime.daysAsCodes.each { day ->

                // Project the date forward.
                def date = getUpcomingWeekday(dayToOffset(day));
                def startDate = setTime(date, 'hh:mma', meetingTime.startTime);
                def endDate = setTime(date, 'hh:mma', meetingTime.endTime);

                events << [startDate: startDate, endDate: endDate];
            }
        }
        return events;
    }

    /**
     * Sets the given date's time to the given format string.
     */
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
        date;
    }

    int dayToOffset(code) {
        def days = ["M", "T", "W", "TH", "F"]
        return days.indexOf(code);
    }
}
