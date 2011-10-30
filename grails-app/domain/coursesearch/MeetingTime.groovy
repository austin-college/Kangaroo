package coursesearch

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

    String daysString() { getDaysAsCodes().join("") }

    List<String> getDaysAsCodes() {
        List<String> days = [];
        if (meetsMonday)
            days << "M";
        if (meetsTuesday)
            days << "T";
        if (meetsWednesday)
            days << "W";
        if (meetsThursday)
            days << "TH";
        if (meetsFriday)
            days << "F";
        days;
    }

    List<String> getDaysAsWords() { getDaysAsCodes().collect { code -> codesToWords[code] } }

    String toString() {
        return "${daysString()} ${startTime} ${endTime}"
    }

    List<Course> getCourses() { CourseMeetingTime.findAllByMeetingTime(this)*.course }
}
