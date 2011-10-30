package coursesearch.data.convert

import coursesearch.MeetingTime

/**
 * Converts meeting schedules from the "MWF 09:00AM 10:00AM" format to (and from) MeetingTimes.
 */
class ScheduleParseService {

    static transactional = true

    /**
     * Converts the given "MWF 09:00AM 10:00AM" string to a MeetingTime. The MeetingTime is NOT yet persisted.
     * Returns null if the MeetingTime could not be parsed.
     */
    static def MeetingTime convertMeetingTime(String composite) {

        try {
            if (composite.contains("TBA"))
                return null;

            def days = composite[0..5];
            def time = composite[6..-1].trim()
            def startTime = time.split(" ")[0];
            def endTime = time.split(" ")[1];

            def properties = new MeetingTime(startTime: startTime, endTime: endTime)

            // Check "TH" first and remove it so "T" does not match.
            if (days.contains("TH"))
                properties.meetsThursday = true;
            days = days.replaceAll("TH", "")

            if (days.contains('M'))
                properties.meetsMonday = true;
            if (days.contains('T'))
                properties.meetsTuesday = true;
            if (days.contains('W'))
                properties.meetsWednesday = true;
            if (days.contains('F'))
                properties.meetsFriday = true;

            return properties;
        }
        catch (Exception e) {
            println "Error parsing schedule $composite..."
        }
    }

    /**
     * Persists a MeetingTime, or finds an exact existing match.
     */
//    @Transactional()
    static def MeetingTime findOrCreate(MeetingTime properties) {

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
}
