package coursesearch.data.convert

import grails.test.GrailsUnitTestCase

class ScheduleParseIntegrationTests extends GrailsUnitTestCase {

    def scheduleParseService

    void testSaveOrFind() {

        def model = ScheduleParseService.convertMeetingTime("MWF   11:00AM 12:20PM")

        def one = ScheduleParseService.findOrCreate(model)
        assert one.toString() == "MWF 11:00AM 12:20PM"
        assert one.id == 1

        def two = ScheduleParseService.findOrCreate(model)
        assert two.toString() == "MWF 11:00AM 12:20PM"
        assert two.id == 1

        def three = ScheduleParseService.findOrCreate(ScheduleParseService.convertMeetingTime("MWF   11:01AM 12:20PM"))
        assert three.toString() == "MWF 11:01AM 12:20PM"
        assert three.id == 2
    }
}
