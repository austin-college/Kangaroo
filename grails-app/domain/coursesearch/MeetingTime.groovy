package coursesearch

import coursesearch.data.convert.ScheduleParseService
import coursesearch.mn.CourseMeetingTime

class MeetingTime implements Serializable {

    static transients = ['daysAsCodes', 'daysAsWords', 'daysAsString']

    boolean meetsMonday, meetsTuesday, meetsWednesday, meetsThursday, meetsFriday;

    String startTime, endTime

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

    List<Course> getCourses() { CourseMeetingTime.findAllByMeetingTime(this)*.course }

    String getDaysAsString() { getDaysAsCodes().join("") }

    List<String> getDaysAsCodes() { ScheduleParseService.getDaysAsCodes(this) }

    List<String> getDaysAsWords() { ScheduleParseService.getDaysAsWords(this) }

    String toString() { "${getDaysAsString()} ${startTime} ${endTime}" }
}
