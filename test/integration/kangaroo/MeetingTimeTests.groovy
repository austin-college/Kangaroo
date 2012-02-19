package kangaroo

import grails.test.GrailsUnitTestCase
import kangaroo.data.convert.ScheduleConvertService

class MeetingTimeTests extends GrailsUnitTestCase {

    void testConstraints() {

        def positives = ["09:00AM", "10:00AM", "12:20PM", "01:15AM", "02:50PM"];
        def negatives = ["9:00AM", "9pm", "10:0AM"];

        positives.each { assert new MeetingTime(startTime: it, endTime: "11:50PM").validate() }
        negatives.each { assert !new MeetingTime(startTime: it, endTime: "11:50PM").validate()}
    }

    void testSaveOrFind() {

        def model = ScheduleConvertService.convertMeetingTime("MWF   11:00AM 12:20PM")

        def one = model.saveOrFind()
        assert one.toString() == "MWF 11:00AM 12:20PM"
        assert one.id == 1

        def two = model.saveOrFind()
        assert two.toString() == "MWF 11:00AM 12:20PM"
        assert two.id == 1

        def three = (ScheduleConvertService.convertMeetingTime("MWF   11:01AM 12:20PM")).saveOrFind()
        assert three.toString() == "MWF 11:01AM 12:20PM"
        assert three.id == 2
    }
}
