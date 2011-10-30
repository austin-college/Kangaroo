package coursesearch.data

import coursesearch.data.convert.ScheduleParseService
import grails.test.GrailsUnitTestCase

/**
 * Tests the schedule converter.
 */
class ScheduleParseServiceTests extends GrailsUnitTestCase {

    def scheduleParseService

    void testMWFParsing() {

        def composite = "MWF   09:00AM 10:00AM"
        def meetingTime = ScheduleParseService.convertMeetingTime(composite)

        assert meetingTime.meetsMonday
        assert !meetingTime.meetsTuesday
        assert meetingTime.meetsWednesday
        assert !meetingTime.meetsThursday
        assert meetingTime.meetsFriday
        assert meetingTime.startTime == "09:00AM"
        assert meetingTime.endTime == "10:00AM"
        assert meetingTime.toString() == "MWF 09:00AM 10:00AM"
    }

    void testTTHParsing() {

        def composite = "TTH   01:30PM 02:50PM"
        def meetingTime = ScheduleParseService.convertMeetingTime(composite)

        assert !meetingTime.meetsMonday && meetingTime.meetsTuesday &&
                !meetingTime.meetsWednesday && meetingTime.meetsThursday && !meetingTime.meetsFriday;

        assert meetingTime.startTime == "01:30PM"
        assert meetingTime.endTime == "02:50PM"
        assert meetingTime.toString() == "TTH 01:30PM 02:50PM"
    }

    void testDaysAsCodes() { assert ScheduleParseService.convertMeetingTime("MWF   08:00AM 09:00AM").daysAsCodes == ['M', 'W', 'F'] }

    void testDaysAsWords() { assert ScheduleParseService.convertMeetingTime("TTH   09:30AM 10:50AM").daysAsWords == ['Tuesday', 'Thursday'] }
}
