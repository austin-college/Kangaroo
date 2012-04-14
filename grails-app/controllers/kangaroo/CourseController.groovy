package kangaroo

import kangaroo.data.BackendDataService

class CourseController {

    def index = {
        redirect(controller: 'home')
    }

    def bySchedule = {

        Map courses = [:]

        def meetingTime = MeetingTime.get(params.id)

        if (meetingTime) {

            // Find all courses at this time, but segment by department.
            def coursesAtTime = meetingTime.coursesMeeting;
            Department.list().each { dept ->
                def coursesFound = coursesAtTime.findAll { it.department.id == dept.id && it.term == BackendDataService.currentTerm }
                if (coursesFound)
                    courses.put(dept, coursesFound)
            }


            [courses: courses, schedule: meetingTime.toString()]
        }
    }

    def byRoom = {
        if (params.id) {

            Map courses = [:]

            // Find all courses at this time by department.
            Department.list().each { dept ->
                def coursesFound = Course.withCriteria {
                    eq("room", params.id)
                    eq("department", dept)
                    eq("term", BackendDataService.currentTerm)
                }
                if (coursesFound)
                    courses.put(dept, coursesFound)
            }

            [courses: courses, room: params.id]
        }
    }

    def show = {
        def course = Course.get(params.id)
        if (course) {
            def profImage = course.instructors[0]?.photoUrl
            [course: course, profImage: profImage]
        }
        else
            redirect(controller: "home")
    }
}
