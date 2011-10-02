package coursesearch

class CourseController {

    def index = {
        redirect(controller: 'home')
    }

    def bySchedule = {
        if (params.id) {
            [courses: Course.findAllBySchedule(params.id), schedule: params.id]
        }
    }

    def show = {
        def course = Course.findByZap(params.id)
        if (course) {
            def profLinks = course.instructors.collect { "<a href=\"${g.createLink(controller: 'professor', action: 'show', id: it.id)}\">${it}</a>"}.join(" and ");

            def fullPercentage = (int) ((double) (course.seatsUsed / course.capacity * 100.0)).round();
            [course: course, profLinks: profLinks, fullPercentage: fullPercentage, fullPercentageColor: getColorForPercent(fullPercentage)]
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
