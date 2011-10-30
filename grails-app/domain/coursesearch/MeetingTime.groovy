package coursesearch

import coursesearch.data.convert.ScheduleParseService
import coursesearch.mn.CourseMeetingTime

class MeetingTime implements Serializable {

    static codesToWords = ["M": "Monday", "T": "Tuesday", "W": "Wednesday", "TH": "Thursday", "F": "Friday"]

    static transients = ['daysAsCodes', 'daysAsWords']

    boolean meetsMonday, meetsTuesday, meetsWednesday, meetsThursday, meetsFriday;

    String startTime

    String endTime

    def MeetingTime saveOrFind() {

        // See if this exact meeting time already exists.
        if (MeetingTime.find(this))
            return MeetingTime.find(this);
        else {
            save();
            return this;
        }
    }

    static constraints = {
    }

    boolean equals(Object other) { toString() == other.toString() }

    String daysString() { getDaysAsCodes().join("") }

    List<String> getDaysAsCodes() { ScheduleParseService.getDaysAsCodes(this) }

    List<String> getDaysAsWords() { getDaysAsCodes().collect { code -> codesToWords[code] } }

    List<Course> getCourses() { CourseMeetingTime.findAllByMeetingTime(this)*.course }

    String toString() { "${daysString()} ${startTime} ${endTime}" }
}
