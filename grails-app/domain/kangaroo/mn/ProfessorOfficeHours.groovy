package kangaroo.mn

import kangaroo.MeetingTime
import kangaroo.Professor
import kangaroo.Term

/**
 * Many-many relationship between professors and office hours, which are stored as MeetingTimes (professor has these office hours).
 */
class ProfessorOfficeHours implements Serializable {

    Term term

    Professor professor

    MeetingTime meetingTime

    static mapping = {
        id(composite: ['term', 'professor', 'meetingTime'])
        version(false)
    }

}
