package kangaroo

class ProfessorSearchController {

    def index = {

        def listByLetters = [:]

        // Divide the list by letter.
        for (char c: 'A'..'Z') {

            def matching = Professor.findAllByLastNameIlikeAndIsProfessor("$c%", true)
            if (matching)
                listByLetters[c] = matching;
        }

        [listByLetters: listByLetters]
    }
}
