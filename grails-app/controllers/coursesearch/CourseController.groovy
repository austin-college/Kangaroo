package coursesearch

class CourseController {

    def index = {
        redirect(controller: 'home')
    }

    def show = {
        def course = Course.findByZap(params.id)
        if (course) {
            def profLinks = course.instructors.collect { "<a href=\"${g.createLink(controller: 'professor', action: 'show', id: it.id)}\">${it}</a>"}.join(" and ");
            [course: course, profLinks: profLinks]
        }
    }
}
