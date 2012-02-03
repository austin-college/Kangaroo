package coursesearch

import grails.converters.JSON
import coursesearch.data.convert.ScheduleProjectService
import coursesearch.data.convert.ScheduleConvertService

import coursesearch.mn.ProfessorOfficeHours

class ProfessorController {

    def dataExportService
    static def days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]

    def index = {
        redirect(controller: 'home')
    }

    def show = {
        def professor = Professor.get(params.id)

        if (professor)
            [professor: professor]
    }

    def printWeeklyCalendar = {
        def professor = Professor.get(params.id)

        if (professor)
            [professor: professor]
    }

    def mobileCalendar = {
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

    def finishedOfficeHours = {
        def professor = Professor.findByPrivateEditKey(params.id);
        if (professor)
            [professor: professor]
    }


    def editOfficeHours = {

        def professor = Professor.findByPrivateEditKey(params?.id);
        if (professor) {

            List<MeetingTime> officeHours = [];

            JSON.parse(params.officeHours).each { data ->
                Date start = Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'ZZZZ", data.start + "-0600");
                Date end = Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'ZZZZ", data.end + "-0600");

                // Adjust for time zones.
                start.hours -= 6;
                end.hours -= 6;

                // Convert to a MeetingTime.
                final days = ["", "SU", "M", "T", "W", "TH", "F", "SA"]
                String composite = days[start[Calendar.DAY_OF_WEEK]] + " " + start.format("hh:mmaa") + " " + end.format("hh:mmaa");
                MeetingTime meetingTime = ScheduleConvertService.convertMeetingTime(composite);

                // See if there's an existing meetingTime with the same times that we can add to. (So we end up with "MWF 3:00PM 4:00PM" instead of three separate ones.)
                boolean matched = false;
                for (MeetingTime existing: officeHours) {

                    if (existing.startTime == meetingTime.startTime && existing.endTime == meetingTime.endTime) {
                        ScheduleConvertService.setDayCodes(existing, meetingTime.daysAsString)
                        matched = true;
                        break;
                    }
                }

                if (!matched)
                    officeHours << meetingTime
            }

            // Remove existing office hours.
            ProfessorOfficeHours.findAllByProfessor(professor).each { it.delete(flush: true)}

            // Add the new ones!
            officeHours.each { meetingTime ->
                meetingTime = meetingTime.saveOrFind()
                new ProfessorOfficeHours(professor: professor, meetingTime: meetingTime).save(flush: true)
            }

            // Export the data so it can be viewed by the iPhone app.
            dataExportService.exportOfficeHours()

            render([success: true] as JSON)
        }
        else
            render([error: "InvalidProfessor"] as JSON)
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
                            url: (params.hideLinks ? null : g.createLink(controller: "course", action: "show", id: course.id))]
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
