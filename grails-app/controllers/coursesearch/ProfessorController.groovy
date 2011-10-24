package coursesearch

class ProfessorController {

    static def days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]

    def index = {
        redirect(controller: 'home')
    }

    def show = {
        def professor = Professor.get(params.id)

        if (professor) {
            [professor: professor, schedule: getSchedule(professor), days: days]
        }
    }

    def getSchedule(Professor professor) {

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

        return schedule
    }
}
