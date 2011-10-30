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

        meetingTime.with {
            assert meetsMonday && !meetsTuesday && meetsWednesday &&
                    !meetsThursday && meetsFriday

            assert startTime == "09:00AM"
            assert endTime == "10:00AM"
        }
        assert meetingTime.toString() == "MWF 09:00AM 10:00AM"
    }

    void testTTHParsing() {

        def composite = "TTH   01:30PM 02:50PM"
        def meetingTime = ScheduleParseService.convertMeetingTime(composite)

        meetingTime.with {
            assert !meetsMonday && meetsTuesday && !meetsWednesday &&
                    meetsThursday && !meetsFriday;

            assert startTime == "01:30PM"
            assert endTime == "02:50PM"
        }
        assert meetingTime.toString() == "TTH 01:30PM 02:50PM"
    }

    void testMParsing() {
        ScheduleParseService.convertMeetingTime("M     01:30PM 02:50PM").with {
            assert meetsMonday && !meetsTuesday && !meetsWednesday && !meetsThursday && !meetsFriday
            assert startTime == "01:30PM"
            assert endTime == "02:50PM"
        }
    }

    void testWithoutSpacing() {
        assert ScheduleParseService.convertMeetingTime("MWF 10:00AM 11:00AM") == ScheduleParseService.convertMeetingTime("MWF   10:00AM 11:00AM")
        assert ScheduleParseService.convertMeetingTime("MTWTHF 10:00AM 11:00AM") == ScheduleParseService.convertMeetingTime("MTWTHF10:00AM 11:00AM")
    }

    void testDaysAsCodes() { assert ScheduleParseService.convertMeetingTime("MWF   08:00AM 09:00AM").daysAsCodes == ['M', 'W', 'F'] }

    void testDaysAsWords() { assert ScheduleParseService.convertMeetingTime("TTH   09:30AM 10:50AM").daysAsWords == ['Tuesday', 'Thursday'] }
}
