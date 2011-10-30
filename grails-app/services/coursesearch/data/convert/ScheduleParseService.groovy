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

            // Make sure that if all the day codes are on, there's a space after them. (WebHopper won't do so)
            if (composite.startsWith("MTWTHF"))
                composite = composite.replaceAll("MTWTHF", "MTWTHF ")

            // Split the string into three parts ("MWF", "09:00AM", "10:00AM") without padding.
            def parts = (List<String>) composite.split(" ").findAll { it.length() > 0 }

            def meetingTime = new MeetingTime(startTime: parts[1], endTime: parts[2])

            // Convert the "MTWTHF" string into meets*** booleans.
            meetingTime.with {

                def days = parts[0];

                // Check "TH" first and remove it so "T" does not match.
                if (days.contains("TH"))
                    meetsThursday = true;
                days = days.replaceAll("TH", "")

                // Check for the rest
                if (days.contains('M'))
                    meetsMonday = true;
                if (days.contains('T'))
                    meetsTuesday = true;
                if (days.contains('W'))
                    meetsWednesday = true;
                if (days.contains('F'))
                    meetsFriday = true;
            }
            return meetingTime;
        }
        catch (Exception e) {
            println "Error parsing schedule $composite..."
        }
    }
}
