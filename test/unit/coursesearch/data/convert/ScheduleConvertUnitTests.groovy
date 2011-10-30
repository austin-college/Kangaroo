package coursesearch.data.convert

import grails.test.GrailsUnitTestCase

/**
 * Tests the schedule converter.
 */
class ScheduleConvertUnitTests extends GrailsUnitTestCase {

    def scheduleConvertService

    void testMWFParsing() {

        def composite = "MWF   09:00AM 10:00AM"
        def meetingTime = ScheduleConvertService.convertMeetingTime(composite)

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
        def meetingTime = ScheduleConvertService.convertMeetingTime(composite)

        meetingTime.with {
            assert !meetsMonday && meetsTuesday && !meetsWednesday &&
                    meetsThursday && !meetsFriday;

            assert startTime == "01:30PM"
            assert endTime == "02:50PM"
        }
        assert meetingTime.toString() == "TTH 01:30PM 02:50PM"
    }

    void testMParsing() {
        ScheduleConvertService.convertMeetingTime("M     01:30PM 02:50PM").with {
            assert meetsMonday && !meetsTuesday && !meetsWednesday && !meetsThursday && !meetsFriday
            assert startTime == "01:30PM"
            assert endTime == "02:50PM"
        }
    }

    void testWithoutSpacing() {
        assert ScheduleConvertService.convertMeetingTime("MWF 10:00AM 11:00AM") == ScheduleConvertService.convertMeetingTime("MWF   10:00AM 11:00AM")
        assert ScheduleConvertService.convertMeetingTime("MTWTHF 10:00AM 11:00AM") == ScheduleConvertService.convertMeetingTime("MTWTHF10:00AM 11:00AM")
    }

    void testDaysAsCodes() { assert ScheduleConvertService.convertMeetingTime("MWF   08:00AM 09:00AM").daysAsCodes == ['M', 'W', 'F'] }

    void testDaysAsWords() { assert ScheduleConvertService.convertMeetingTime("TTH   09:30AM 10:50AM").daysAsWords == ['Tuesday', 'Thursday'] }
}
