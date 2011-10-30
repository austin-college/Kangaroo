package coursesearch.mn

import coursesearch.Professor
import coursesearch.MeetingTime

class ProfessorOfficeHours implements Serializable {

    Professor professor

    MeetingTime meetingTime

    static mapping = {
        id(composite: ['professor', 'meetingTime'])
        version(false)
    }

}
