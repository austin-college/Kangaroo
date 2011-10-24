package coursesearch

class ProfessorController {

    def index = {
        redirect(controller: 'home')
    }

    def show = {
        def professor = Professor.get(params.id)
        getSchedule(professor)
        if (professor) {
            [professor: professor]
        }
    }

    def getSchedule(Professor professor) {

        def days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]
        def schedule = ["Monday": [], "Tuesday": [], "Wednesday": [], "Thursday": [], "Friday": []];

        // Get all of this professor's meeting times.
        List<MeetingTime> meetingTimes = []
        professor.coursesTeaching.each { course -> meetingTimes.addAll(course.meetingTimes) }
    }
}
