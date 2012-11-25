package kangaroo.setup.prefill

import kangaroo.Professor
import kangaroo.Term
import kangaroo.data.convert.ScheduleConvertService
import kangaroo.mn.ProfessorOfficeHours

class OfficeHoursDataService extends UpdateableDataService {

    static String name = "Office Hours list"
    static String url = "${dataRoot}/officeHours/${Term.CURRENT_TERM_CODE}.json"
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