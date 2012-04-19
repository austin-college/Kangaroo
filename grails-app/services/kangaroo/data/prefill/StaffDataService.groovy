package kangaroo.data.prefill

import kangaroo.Professor

/**
 * Just a copy of the faculty fetcher, but for staff. (Fix that).
 */
class StaffDataService extends FacultyDataService {

    static transactional = true
    static String name = "Staff list"
    static String url = "${dataRoot}/staff.json"
    static int lastVersionUsed = 0;

    @Override
    protected void upgradeAll(dataFromServer) {
        run(dataFromServer.list);
    }

    @Override
    protected void map(Professor professor, Map details) {
        super.map(professor, details)
        professor.isProfessor = false;
    }
}