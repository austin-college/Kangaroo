package kangaroo.data.prefill

import kangaroo.mn.ProfessorOfficeHours
import kangaroo.Professor
import kangaroo.data.BackendDataService
import kangaroo.data.convert.ScheduleConvertService
import kangaroo.Term


class OfficeHoursDataService extends UpdateableDataService {

    static String name = "Office Hours list"
    static String url = "${dataRoot}/officeHours/${Term.currentTerm.id}.json"
    static int lastVersionUsed = 0;

    @Override
    protected void upgradeAll(dataFromServer) {

        // Only import if empty.
        if (ProfessorOfficeHours.countByTerm(Term.currentTerm) > 0)
            return;

        // Add the new ones.
        dataFromServer.list.each { id, blocks ->
            def prof = Professor.get(id)

            if (prof) {
                blocks.each { block ->
                    new ProfessorOfficeHours(professor: prof, term: Term.currentTerm, meetingTime: ScheduleConvertService.convertMeetingTime(block).saveOrFind()).save()
                    prof.save();
                }
            }
        }
    }

}