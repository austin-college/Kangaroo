package coursesearch.data.prefill

import coursesearch.Major
import coursesearch.mn.ProfessorOfficeHours
import coursesearch.Professor
import coursesearch.data.convert.ScheduleConvertService

class OfficeHoursDataService extends UpdateableDataService {

    static String name = "Office Hours list"
    static String url = "${dataRoot}/officeHours.json"
    static int lastVersionUsed = 0;

    @Override
    protected void upgradeAll(dataFromServer) {

        // Remove the existing items.
        ProfessorOfficeHours.list().each { it.delete(flush: true) }

        // Add the new ones.
        dataFromServer.list.each { data ->
            def prof = Professor.findByName(data.name);

            if (prof) {
                data.hours.each { schedule ->
                    new ProfessorOfficeHours(professor: prof, meetingTime: ScheduleConvertService.convertMeetingTime(schedule).saveOrFind()).save()
                    prof.save();
                }
            }
        }
    }

}
