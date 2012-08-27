package kangaroo

import grails.converters.JSON
import javax.servlet.http.Cookie
import kangaroo.data.convert.ScheduleConvertService
import kangaroo.data.convert.ScheduleProjectService
import kangaroo.mn.ProfessorOfficeHours
import kangaroo.data.BackendDataService
import grails.plugins.springsecurity.Secured

import org.springframework.security.core.userdetails.User

class ProfessorController {

	def springSecurityService
	
    static def days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]

    def index = {
        redirect(controller: "professorSearch")
    }

    def show = {
        def professor = Professor.get(params.id)
		
        if (professor)
            [professor: professor]
        else
            redirect(controller: "home")
    }

    def printWeeklyCalendar = {
        def professor = Professor.get(params.id)

        if (professor)
            [professor: professor]
    }

	
	private User currentUser() {
		return springSecurityService.principal
	}
	
	
	private boolean userIsProfessor(Professor prof) {
		if (prof == null) return false;
		
		def user = currentUser()
		if (user == null) return false;
		
		return prof.id.equals(user.username);
	}
	
	/**
	 * called to prepare the view to allow a user to edit office hours and office note.  Eventually, this 
	 * may be expanded to allow the user to edit other things.
	 * 
	 */
	@Secured(['ROLE_FACULTY','IS_AUTHENTICATED_FULLY'])
    def setOfficeHours = {

        def professor = Professor.get(params.id)
		
        if (professor) {

			// the professor must be the authenticated user.			
			if (!userIsProfessor(professor)) {
				flash.message = "User not authorized for modifying ${professor} profile."
				return redirect(controller: "professor", action: "show", id: "${professor.id}")
		
			}  
			
            session.professorId = professor.id;
            response.addCookie(createCookie("prof_id", professor.id, 365))

			[professor: professor]
			
        }
        else {
			flash.message = "Unknown id."
			return redirect(controller: "home")
        }
    }
	
	
    Cookie createCookie(key, value, maxAgeInDays) {
        def c = new Cookie(key, value)
        c.maxAge = maxAgeInDays * 24 * 60 * 60
        c.path = "/"
        c;
    }

	
	@Secured(['ROLE_FACULTY','IS_AUTHENTICATED_FULLY'])
    def finishedOfficeHours = {
		def professor = Professor.get(params.id)
        if (professor) {
			
			if (!userIsProfessor(professor)) {
				flash.message = "User not authorized for modifying ${professor} profile."
				return redirect(controller: "professor", action: "show", id: "${professor.id}")
			}

            [professor: professor]
        }
    }


	@Secured(['ROLE_FACULTY','IS_AUTHENTICATED_FULLY'])
    def editOfficeHours = {
		
		def professor = Professor.get(params.id)
        
        if (professor) {

			if (!userIsProfessor(professor)) {
				flash.message = "User not authorized for modifying ${professor} profile."
				return redirect(controller: "professor", action: "show", id: "${professor.id}")
			}
						
			professor.officeNote = params.note
			professor.save(flush:true)

            List<MeetingTime> officeHours = [];

            JSON.parse(params.officeHours).each { data ->
                Date start = Date.parse("yyyy-MM-dd'T'HH:mm:ss", data.start);
                Date end = Date.parse("yyyy-MM-dd'T'HH:mm:ss", data.end);

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
                new ProfessorOfficeHours(term: Term.currentTerm, professor: professor, meetingTime: meetingTime).save(flush: true)
            }

            render([success: true] as JSON)
        }
        else
            render([error: "InvalidProfessor"] as JSON)
    }

    def getStatus = {
        def professor = Professor.get(params.id)

        if (professor)
            render([html: g.render(template: "status", model: [status: professor.status])] as JSON)
    }

    /**
     * Returns a JSON list of this professor's class schedule for the week.
     */
    def getSchedule = {

        def professor = Professor.get(params.id);

        if (professor) {

            def events = []
            def usedTimes = [:]

            // For every course, convert its meeting times into real dates...
            professor.currentCursesTeaching.each { course ->
                ScheduleProjectService.projectToWeek(course.meetingTimes).each { time ->

                    // ...then add them to the calendar.
                    if (!usedTimes.containsKey(time)) { // Don't show overlapping classes.
                        usedTimes[time] = course;
                        events << [title: course.name, allDay: false, start: time.startDate, end: time.endDate,
                                url: (params.hideLinks ? null : g.createLink(controller: "course", action: "show", id: course.id))]
                    }
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
