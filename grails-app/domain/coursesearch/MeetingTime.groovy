package coursesearch

import coursesearch.mn.CourseMeetingTime

class MeetingTime implements Serializable {

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
            println "${mt.errors}";
            mt;
        }
    }

    String daysString() {
        def string = "";
        if (meetsMonday)
            string += "M";
        if (meetsTuesday)
            string += "T";
        if (meetsWednesday)
            string += "W";
        if (meetsThursday)
            string += "TH";
        if (meetsMonday)
            string += "F";
        string
    }

    String toString() {
        return "${daysString()} ${startTime} ${endTime}"
    }

    List<Course> getCourses() { CourseMeetingTime.findAllByMeetingTime(this)*.course }
}
