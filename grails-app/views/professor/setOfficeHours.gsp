<%@ page import="coursesearch.Term; coursesearch.CourseUtils; coursesearch.Course" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${professor}: Set your office hours</title>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'libraries/fullcalendar', file: 'fullcalendar.css')}"/>
    <g:javascript src="../libraries/fullcalendar/fullcalendar.js"/>
    <script type="text/javascript">
        var timelineInterval;
        var calendar;
        $(document).ready(function () {


            calendar = $('#calendar').fullCalendar({
                weekends:false,
                eventSources:[

                    {
                        url:contextPath + "/professor/getOfficeHours/${professor.id}",
                        color:'green'
                    },
                    {
                        url:contextPath + "/professor/getSchedule/${professor.id}",
                        color:'#36c'
                    }

                ],
                columnFormat: { week: "dddd's'" /* "Mondays" */ },
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
                        $(".finishButtonRight").fadeIn();
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
                }
            });
        });

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
        position: absolute;
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
        margin-top: 24px;
        margin-right: 10px;
    }

    .finishButtonRight .btn {
        font-size: 20px;
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

    <div class="finishButtonRight" style="display: none;">
        <button class="btn primary large">Save and finish &raquo;</button>
    </div>

    <div class="span7 info">
        <h1>Welcome, ${professor}!</h1>

        <div>Set your office hours below.</div>
    </div>

    <div style="clear: both; margin-top: 140px;"></div>

    <div>

        <div id="calendar"></div>
    </div>
</div>

</body>
</html>