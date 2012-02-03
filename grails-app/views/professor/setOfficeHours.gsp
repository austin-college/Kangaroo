<%@ page import="coursesearch.Term; coursesearch.CourseUtils; coursesearch.Course" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${professor.firstName}: Set your office hours</title>
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
                        url:contextPath + "/professor/getOfficeHours/${professor.id}?hideLinks=true",
                        color:'green',
                        editable:true
                    },
                    {
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

                    window.clearInterval(timelineInterval);
                    timelineInterval = window.setInterval(setTimeline, 10000);
                    setTimeline();
                },
                eventDrop:function (event, dayDelta, minuteDelta, allDay, revertFunc) {
                    calendarChanged();
                },
                eventResize:function (event, dayDelta, minuteDelta, revertFunc) {
                    calendarChanged();
                },
                eventClick:function (calEvent, jsEvent, view) {

                    if (calEvent.source.editable && confirm("Remove this block of office hours?")) {
                        calendarChanged();
                        calendar.fullCalendar('removeEvents', function (event) {
                            return ( event == calEvent );
                        });
                    }
                }
            });

            $("a.startOver").click(function () {

                calendar.fullCalendar('removeEvents');
                calendar.fullCalendar('refetchEvents');
                $(".finishButtonRight").hide();

                return false;
            });
        });

        function calendarChanged() {
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
        <button class="btn primary large">Save and finish &raquo;</button>

        <div>
            <a href="#" class="startOver">Start over &raquo;</a>
        </div>
    </div>

    <div class="span8 info">
        <h1>Welcome, ${professor.firstName}!</h1>

        <div>To set your office hours, just <b>click</b> and <b>drag</b> on the calendar.</div>
    </div>

    <div style="clear: both; margin-top: 140px;"></div>

    <div>

        <div id="calendar"></div>
    </div>
</div>

</body>
</html>