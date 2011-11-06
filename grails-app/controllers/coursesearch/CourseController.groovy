package coursesearch

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
                def coursesFound = coursesAtTime.findAll { it.department.id == dept.id && it.term == Term.findOrCreate("12SP") }
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
                    eq("term", Term.findOrCreate("12SP"))
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
            def fullPercentage = (int) ((double) (course.seatsUsed / course.capacity * 100.0)).round();
            [course: course, profImage: profImage, fullPercentage: fullPercentage, fullPercentageColor: getColorForPercent(fullPercentage)]
        }
    }

    String getColorForPercent(int percent) {
        switch (percent) {
            case 0..60: return '#3c0';
            case 61..70: return '#6a0';
            case 71..80: return '#990';
            case 81..90: return '#a60';
            case 91..100: return '#b30';
            default: return '#d30';
        }
    }
}
