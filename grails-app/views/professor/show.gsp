<%@ page import="coursesearch.CourseUtils; coursesearch.Course" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${professor}</title>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'data_table.css')}"/>
    <link rel="stylesheet" href="${resource(dir: 'libraries/fullcalendar', file: 'fullcalendar.css')}"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
    <g:javascript src="../libraries/fullcalendar/fullcalendar.js"/>
    <script type="text/javascript">
        var timelineInterval;
        var calendar;
        var date = new Date();
        $(document).ready(function() {

            calendar = $('#calendar').fullCalendar({
                weekends: false,
                events: contextPath + "/professor/getSchedule/${professor.id}",
                defaultView: 'agendaWeek',
                header: null,
                ignoreTimezone: false,
                height: 350,
                allDaySlot: false,
                minTime: 8,
                maxTime: 18,
                timeFormat: '',
                viewDisplay: function(view) {

                    date.setHours(8)
                    date.setMinutes(0);
                    window.clearInterval(timelineInterval);
                    timelineInterval = setInterval(setTimeline, 30);
                    // timelineInterval = window.setInterval(setTimeline, 10000);
                    // setTimeline();
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

            var curTime = date;
            date.setMinutes(date.getMinutes() + 1);
            if (date.getMinutes() % 60 == 0) {
                window.clearInterval(timelineInterval);
                timelineInterval = setInterval(setTimeline, 500);
            }
            else {
                window.clearInterval(timelineInterval);
                timelineInterval = setInterval(setTimeline, 30);
            }
            var curCalView = $('#calendar').fullCalendar("getView");
            if (curCalView.visStart < curTime && curCalView.visEnd > curTime) {
                timeline.show();
            } else {
                timeline.hide();
            }

            $("#timeReadout").text($("#timeReadout").text($.fullCalendar.formatDate(curTime, "h:mmtt")));
            var curSeconds = (curTime.getHours() * 60 * 60) + (curTime.getMinutes() * 60) + curTime.getSeconds() - (8 * 60 * 60);
            var percentOfDay = curSeconds / (10 * 60 * 60); //24 * 60 * 60 = 86400, # of seconds in a day
            var topLoc = Math.floor(parentDiv.height() * percentOfDay);

            timeline.css("top", topLoc + "px");

            if (curCalView.name == "agendaWeek") { //week view, don't want the timeline to go the whole way across
                var dayCol = $(".fc-today:visible");
                var left = dayCol.position().left + 1;
                var width = dayCol.width();
                timeline.css({
                    left: left + "px",
                    width: width + "px"
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

<ul class="breadcrumb">
    <li><g:link controller="home">Professors</g:link> <span class="divider">/</span></li>
    <li class="active">${professor}</li>
</ul>

<div>
    <div class="details-block professor-block">

        <g:if test="${professor.photoUrl}">
            <div class="span3 photo">
                <img src="${professor.photoUrl}" alt="${professor}" title="${professor}">
            </div>
        </g:if>

        <div class="span8 info">
            <h1>${professor}</h1>

            <h3>${professor.title}</h3>

            <g:if test="${professor.email}">
                <div><b>E-mail:</b> <a href="mailto:${professor.email}">${professor.email}</a></div>
            </g:if>

            <g:if test="${professor.office}">
                <div><b>Office:</b> ${professor.office}</div>
            </g:if>

            <g:if test="${professor.phone}">
                <div><b>Phone:</b> ${professor.phone}</div>
            </g:if>
        </div>
    </div>
</div>

<div class="details-block courses-block span5">

    <g:if test="${professor.coursesTeaching.size() > 0}">
        <div>
            <h3>${professor} is teaching:</h3>

            <ul>
                <g:each in="${professor.coursesTeaching}" var="course">
                    <li><g:link controller="course" action="show"
                                id="${course.zap}">${course}</g:link> (${course.sectionString()})</li>
                </g:each>
            </ul>
        </div>
    </g:if>
    <g:else>
        <div><h3>${professor} is not teaching any classes</h3></div>
    </g:else>
</div>


<div class="details-block courses-block">

    <g:if test="${professor.colleagues.size() > 0}">
        <div>
            <h3>Colleagues:</h3>

            <ul>
                <g:each in="${professor.colleagues}" var="colleague">
                    <li><g:link controller="professor" action="show"
                                id="${colleague.id}">${colleague}</g:link> (${colleague.activeDepartments.join(", ")})</li>
                </g:each>
            </ul>
        </div>
    </g:if>
    <g:else>
        <div><h3>${professor} has no colleagues</h3></div>
    </g:else>
</div>

<g:if test="${professor.coursesTeaching.size() > 0}">
    <div class="details-block courses-block span14">

        <div>
            <h3 id="timeReadout">Time</h3>

            <div id="calendar"></div>

            %{--<g:each in="${schedule.keySet()}" var="day">--}%
            %{--<b>${day}</b>--}%
            %{--<ul>--}%
            %{--<g:each in="${schedule[day]}" var="scheduleItem">--}%
            %{--<li>--}%

            %{--${scheduleItem.time.startTime} to ${scheduleItem.time.endTime} &middot;--}%

            %{--<g:link controller="course" action="show" id="${scheduleItem.course.id}">--}%
            %{--${scheduleItem.course}--}%
            %{--</g:link>--}%
            %{--</li>--}%
            %{--</g:each>--}%
            %{--</ul>--}%
            %{--</g:each>--}%
        </div>
    </div>
</g:if>
</body>
</html>