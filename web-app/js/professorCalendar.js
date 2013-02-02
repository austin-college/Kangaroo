//==========================================================================
//
// professorCalendar.js
//
// Creates & renders a weekly calendar for the given professor.
//
//==========================================================================

var timelineInterval;
var calendar;

$(document).ready(function () {

    loadProfessorStatus();

    calendar = $('#calendar').fullCalendar({
        weekends:false,
        eventSources:[
            {
                url:document.Kangaroo.url("/professor/getSchedule/" + professorId)
            },
            {
                url:document.Kangaroo.url("/professor/getOfficeHours/" + professorId),
                color:'green'
            }
        ],
        defaultView:'agendaWeek',
        header:null,
        ignoreTimezone:false,
        height:350,
        allDaySlot:false,
        minTime:8,
        maxTime:18,
        timeFormat:'',
        viewDisplay:function (view) {

            window.clearInterval(timelineInterval);
            timelineInterval = window.setInterval(setTimeline, 10000);
            setTimeline();
        }
    });
});

function loadProfessorStatus() {
    $.ajax({
        url:document.Kangaroo.url("/professor/getStatus/" + professorId + "?time=" + new Date().getTime()),
        success:function (response) {
            $("#statusHolder").html(response.html);
            $("#statusHolder").hide();
            $("#statusHolder").fadeIn();
        }
    });
}

function setTimeline() {

    var parentDiv = $(".fc-agenda-slots:visible").parent();
    var timeline = parentDiv.children(".timeline");
    if (timeline.length == 0) { //if timeline isn't there, add it
        timeline = $("<hr>").addClass("timeline");
        parentDiv.prepend(timeline);
    }

    var curTime = new Date();
    var curCalView = $('#calendar').fullCalendar("getView");
    if (curCalView.visStart < curTime && curCalView.visEnd > curTime) {
        timeline.show();
    } else {
        timeline.hide();
    }

    var curSeconds = (curTime.getHours() * 60 * 60) + (curTime.getMinutes() * 60) + curTime.getSeconds() - (8 * 60 * 60);
    var percentOfDay = curSeconds / (10 * 60 * 60); //24 * 60 * 60 = 86400, # of seconds in a day
    var topLoc = Math.floor(parentDiv.height() * percentOfDay);

    timeline.css("top", topLoc + "px");

    if (curCalView.name == "agendaWeek") { //week view, don't want the timeline to go the whole way across
        var dayCol = $(".fc-today:visible");
        var left = dayCol.position().left + 1;
        var width = dayCol.width();
        timeline.css({
            left:left + "px",
            width:width + "px"
        });
    }
}