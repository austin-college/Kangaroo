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

    String toString() { "$daysAsString $startTime $endTime" }

    // Returns the courses meeting at this time.
    List<Course> getCoursesMeeting() { CourseMeetingTime.findAllByMeetingTime(this)*.course }

    // Returns a list of all of the days we meet, as codes (["M", "W", "TH"]).
    List<String> getDaysAsCodes() { ScheduleParseService.getDaysAsCodes(this) }

    // Returns a list of all of the days we meet, as words (["Monday", "Wednesday", "Thursday"]).
    List<String> getDaysAsWords() { ScheduleParseService.getDaysAsWords(this) }

    // Returns a list of all of the days we meet, as a flat string ("MWTH").
    String getDaysAsString() { getDaysAsCodes().join("") }
}
