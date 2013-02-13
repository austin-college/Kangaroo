package kangaroo.data

import grails.converters.JSON
import kangaroo.Professor

class DataExportService {

    static final String formatVersion = "1.0"

    static transactional = true

    def export() {

        def dataFile = [
                name: "Kangaroo Export",
                version: formatVersion,
                dateRun: new Date(),
                officeHours: Professor.list().collectEntries { professor -> [professor.id, professor.officeHours*.toString()] }
        ]

        (dataFile as JSON);
    }
}
