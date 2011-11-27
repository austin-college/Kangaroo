package coursesearch.data.prefill

import coursesearch.Major
import coursesearch.mn.ProfessorOfficeHours

class OfficeHoursDataService extends UpdateableDataService {

    static String name = "Office Hours list"
    static String url = "${dataRoot}/officeHours.json"
    static int lastVersionUsed = 0;

    @Override
    protected void upgradeAll(dataFromServer) {

        // Remove the existing items.
        ProfessorOfficeHours.list().each { it.delete(flush: true) }

        // Add the new ones.
        dataFromServer.majors.each { importMajor(it) }
    }

}
