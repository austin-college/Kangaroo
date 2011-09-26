package coursesearch

class ProfessorController {

    def index = {
        redirect(controller: 'home')
    }

    def show = {
        def professor = Professor.get(params.id)
        if (professor) {
            def classLinks = professor.coursesTeaching.collect { "<a href=\"${g.createLink(controller: 'course', action: 'show', id: it.zap)}\">${it}</a>"}.join(" and ");
            [professor: professor, classLinks: classLinks]
        }
    }
}
