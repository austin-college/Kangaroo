package coursesearch.mn

import coursesearch.Course
import coursesearch.MeetingTime

class CourseMeetingTime implements Serializable {

    Course course

    MeetingTime meetingTime

    static mapping = {
        id(composite: ['course', 'meetingTime'])
        version(false)
    }
}
