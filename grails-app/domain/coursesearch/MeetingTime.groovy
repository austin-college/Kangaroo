package coursesearch

import coursesearch.mn.CourseMeetingTime

class MeetingTime implements Serializable {

    static codesToWords = ["M": "Monday", "T": "Tuesday", "W": "Wednesday", "TH": "Thursday", "F": "Friday"]

    static transients = ['daysAsCodes', 'daysAsWords']

    boolean meetsMonday, meetsTuesday, meetsWednesday, meetsThursday, meetsFriday;

    String startTime

    String endTime

    static constraints = {
    }

    static MeetingTime findOrCreate(MeetingTime properties) {

        // See if this exact meeting time already exists.
        if (MeetingTime.find(properties))
            return MeetingTime.find(properties);
        else {
            def mt = new MeetingTime(meetsMonday: properties.meetsMonday, meetsTuesday: properties.meetsTuesday, meetsWednesday: properties.meetsWednesday,
                    meetsThursday: properties.meetsThursday, meetsFriday: properties.meetsFriday, startTime: properties.startTime, endTime: properties.endTime);

            mt.save()
            mt;
        }
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
