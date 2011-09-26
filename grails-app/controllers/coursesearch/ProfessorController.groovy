package coursesearch

class ProfessorController {

    def index = {
        redirect(controller: 'home')
    }

    def show = {
        def professor = Professor.get(params.id)
        if (professor) {
            [professor: professor]
        }
    }
}
