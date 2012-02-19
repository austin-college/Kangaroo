<%@ page import="kangaroo.Term; kangaroo.CourseUtils; kangaroo.Course" contentType="text/html;charset=UTF-8" %>
<html>
<head>
<title>${professor}: Set your office hours</title>
<meta name="layout" content="main"/>
<link rel="stylesheet" href="${resource(dir: 'libraries/fullcalendar', file: 'fullcalendar.css')}"/>
<g:javascript src="../libraries/fullcalendar/fullcalendar.js"/>
<g:javascript src="../libraries/fullcalendar/jquery-ui-1.8.11.custom.min.js"/>

<script type="text/javascript">

    var timelineInterval;
    var calendar;
    $(document).ready(function () {


        calendar = $('#calendar').fullCalendar({
            weekends:false,
            eventSources:[
                {
                    id:"officeHours",
                    url:contextPath + "/professor/getOfficeHours/${professor.id}?hideLinks=true",
                    color:'green',
                    editable:true
                },
                {
                    id:"classes",
                    url:contextPath + "/professor/getSchedule/${professor.id}?hideLinks=true",
                    color:'#36c',
                    editable:false
                }

            ],
            columnFormat:{ week:"dddd's'" /* "Mondays" */ },
            defaultView:'agendaWeek',
            selectable:true,
            selectHelper:true,
            select:function (start, end, allDay) {
                var title = "Office Hours";
                if (title) {
                    calendar.fullCalendar('renderEvent',
                            {
                                title:title,
                                start:start,
                                end:end,
                                allDay:allDay,
                                color:'green'
                            },
                            true // make the event "stick"
                    );
                    calendarChanged();
                }
                calendar.fullCalendar('unselect');
            },
            eventColor:"green",
            editable:true,
            header:null,
            ignoreTimezone:false,
            height:500,
            allDaySlot:false,
            minTime:8,
            maxTime:18,
            timeFormat:'',
            viewDisplay:function (view) {
            },
            eventDrop:function (event, dayDelta, minuteDelta, allDay, revertFunc) {
                calendarChanged();
            },
            eventResize:function (event, dayDelta, minuteDelta, revertFunc) {
                mpq.track('resized office hours', {'mp_note':"User resized a block of office hours"});
                calendarChanged();
            },
            eventClick:function (calEvent, jsEvent, view) {

                if (calEvent.source.id != "classes" && confirm("Remove this block of office hours?")) {
                    calendarChanged();
                    mpq.track('remove office hours', {'mp_note':"User removed an existing block of office hours"});
                    calendar.fullCalendar('removeEvents', function (event) {
                        return ( event == calEvent );
                    });
                }
            }
        });

        $("a.startOver").click(function () {

            // Reset the calendar...
            mpq.track('reset office hours', {'mp_note':"User started over while setting office hours"});
            calendar.fullCalendar('removeEvents');
            calendar.fullCalendar('refetchEvents');
            $(".finishButtonRight").hide();

            return false;
        });

        $("#finishButton").click(function () {

            mpq.track('finished office hours', {'mp_note':"User finished setting office hours"});
            $("#finishButton").attr("disabled", "disabled");
            $(".finishButtonRight").css({ opacity:0.3 });

            // Extract the office hours...
            var items = []
            $.each(calendar.fullCalendar('clientEvents'), function (index, value) {
                if (value.source.id != "classes") {
                    items.push({start:value.start, end:value.end});
                }
            });

            // Send to the server.
            $.ajax({
                url:contextPath + "/professor/editOfficeHours/${professor.privateEditKey}",
                dataType:"json",
                data:{officeHours:JSON.stringify(items)},
                type:"POST",
                success:function (response) {
                    window.location = contextPath + "/professor/finishedOfficeHours/${professor.privateEditKey}";
                },
                error:function (error) {
                    document.write(error.responseText);
                }
            });
        });

        mpq.track('start office hours', {'mp_note':"User loaded the 'set office hours' page"});
    });

    function calendarChanged() {
        mpq.track('edit office hours', {'mp_note':"User edited office hours (generic)"});
        $(".finishButtonRight").fadeIn();
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
//            alert(curCalView.visStart);
        if (curCalView.visStart < curTime && curCalView.visEnd > curTime) {
            timeline.show();
        } else {
            timeline.hide();
        }

        var curSeconds = (curTime.getHours() * 60 * 60) + (curTime.getMinutes() * 60) + curTime.getSeconds() - (8 * 60 * 60);
        var percentOfDay = curSeconds / (10 * 60 * 60); //24 * 60 * 60 = 86400, # of seconds in a day
        var topLoc = Math.floor(parentDiv.height() * percentOfDay);

        if (topLoc > 0)
            timeline.show();
        else
            timeline.hide();

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
</script>
<style type="text/css">
.timeline {
    positon: absolute;
    left: 59px;
    border: none;
    border-top: 2px solid red;
    width: 100%;
    margin: 0;
    padding: 0;
    z-index: 999;
}

table.fc-agenda-slots {
    margin-bottom: 0;
}

.finishButtonRight {
    float: right;
    display: none;
    margin-top: 24px;
    margin-right: 15px;
}

.finishButtonRight .btn {
    font-size: 20px;
}

a.startOver {
    display: inline-block;
    text-align: center;
    margin-top: 7px;
    margin-left: 48px;
}
</style>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Professors</g:link> <span class="divider">/</span></li>
    <li class="active">${professor}</li>
</ul>

<div class="details-block professor-block span14">

    <g:if test="${professor.photoUrl}">
        <div class="span1 photo" style="margin-right: 20px;">
            <img src="${professor.photoUrl}" alt="${professor}" title="${professor}" width="60px">
        </div>
    </g:if>

    <div class="finishButtonRight">
        <button class="btn primary large" id="finishButton">Save and finish &raquo;</button>

        <div>
            <a href="#" class="startOver">Start over &raquo;</a>
        </div>
    </div>

    <div class="span8 info">

        <g:if test="${professor.id == 'dwilliams'}">
            <h1>Welcome, E. Don!</h1>
        </g:if>
        <g:else>
            <h1>Welcome, ${professor.firstName}!</h1>
        </g:else>

        <div>To set your office hours, just <b>click</b> and <b>drag</b> on the calendar.</div>
    </div>

    <div style="clear: both; margin-top: 140px;"></div>

    <div>

        <div id="calendar"></div>
    </div>
</div>

</body>
</html>