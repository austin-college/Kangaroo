<%@ page import="kangaroo.Term; courkangaroorseUtils; kangaroo.Course" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${professor}</title>
    <meta name="layout" content="bare"/>
    <link rel="stylesheet" href="${resource(dir: 'libraries/fullcalendar', file: 'fullcalendar.css')}"/>
    <g:javascript src="../libraries/fullcalendar/fullcalendar.js"/>
    <script type="text/javascript">
        var timelineInterval;
        var calendar;
        $(document).ready(function () {


            $.ajax({
                url:contextPath + "/professor/getStatus/${professor.id}?time=" + new Date().getTime(),
                success:function (response) {
                    $("#statusHolder").html(response.html);
                    $("#statusHolder").hide();
                    $("#statusHolder").fadeIn();
                }
            });

            calendar = $('#calendar').fullCalendar({
                weekends:false,
                eventSources:[

                    { url:contextPath + "/professor/getSchedule/${professor.id}" },

                    {
                        url:contextPath + "/professor/getOfficeHours/${professor.id}",
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

        function setTimeline() {
            var parentDiv = $(".fc-agenda-slots:visible").parent();
            var timeline = parentDiv.children(".timeline");
            if (timeline.length == 0) { //if timeline isn't there, add it
                timeline = $("<hr>").addClass("timeline");
                parentDiv.prepend(timeline);
            }

            var curTime = new Date();
            var curCalView = $('#calendar').fullCalendar("getView");
//            alert(curCalView.visEnd);
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
    </style>
</head>

<body>

<div class="details-block courses-block span14">

    <div>
        <h3>Schedule</h3>

        <div id="calendar"></div>
    </div>
</div>
</body>
</html>