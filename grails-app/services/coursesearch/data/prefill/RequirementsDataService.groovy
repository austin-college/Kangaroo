package coursesearch.data.prefill

import coursesearch.Requirement

class RequirementsDataService {

    static transactional = true

    static def requirements = [
            "HU": "Humanities",
            "SC": "Science",
            "SS": "Social Science",
            "NLS": "Non-Lab Science",
            "Q": "Quantitative",
            "L": "Language",
            "R": "Half Writing",
            "W": "Full Writing",
    ]

    static def interdisciplinaries = [
            "AS": "Asian Studies",
            "ES": "Environmental Studies",
            "GN": "Gender Studies",
            "LA": "Latin American Studies",
            "SW": "Southwest and Mexican Studies",
            "WIT": "Western Intellectual Tradition",
            "FLM": "Film Studies",
            "CSP": "Community Service and Policy",
            "AM": "American Studies",
            "COG": "Cognitive Science",
            "GSTS": "Global Science, Technology and Society",
            "LD": "Leadership",
            "TOP": "Topics"
    ]

    def fillRequirements() {

        // Create requirements from the mappings.
        createFromMapping(requirements, false);
        createFromMapping(interdisciplinaries, true);
    }

    def createFromMapping(mapping, markAsDiscipline) {
        mapping.each { code, name ->
            def req = Requirement.findByCode(code);
            if (req) {
                if (req.name != name || req.isInterdisciplinaryMajor != markAsDiscipline) { // Update this requirements's name.
                    req.name = name
                    req.isInterdisciplinaryMajor = markAsDiscipline
                    req.save();
                }
            }
            else
                new Requirement(code: code, name: name, isInterdisciplinaryMajor: markAsDiscipline).save();
        }
    }
}
