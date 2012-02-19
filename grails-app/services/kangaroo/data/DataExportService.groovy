package kangaroo.data

import kangaroo.Professor

import grails.converters.JSON
import grails.util.Environment

class DataExportService {

    static transactional = true

    def exportOfficeHours() {

        def term = BackendDataService.currentTerm;

        def list = []

        Professor.list().each { professor ->
            if (professor.officeHours)
                list << [id: professor.id, name: professor.name, hours: professor.officeHours*.toString()]
        }

        def dataFile = [
                name: "officeHours.json",
                description: "Defines professor's office hours for the given term.",
                term: term.shortCode,
                version: 1,
                formatVersion: 1,
                list: list
        ]

        // Write it to the temporary hosting area.
        if (Environment == Environment.PRODUCTION) {
            final String PRODUCTION_FILE_ROOT = "C:\\Web\\nginx\\html\\data"

            new File("$PRODUCTION_FILE_ROOT\\officeHours").mkdir()
            new File("$PRODUCTION_FILE_ROOT\\officeHours\\${term.shortCode}.json").write((dataFile as JSON).toString(true));
        }
    }
}
