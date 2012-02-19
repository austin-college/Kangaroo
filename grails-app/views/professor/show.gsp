<%@ page import="kangaroo.AppUtils; kangaroo.Professor; kangaroo.Term; kangaroo.AppUtils; kangaroo.Course" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${professor}</title>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'libraries/fullcalendar', file: 'fullcalendar.css')}"/>
    <g:javascript src="../libraries/fullcalendar/fullcalendar.js"/>
    <less:stylesheet name="profiles"/>
    <less:stylesheet name="professorView"/>
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

            if (${session.professorId && professor.id == session.professorId})
                mpq.track('self view', {'mp_note':"User viewed their own profile."});
            else
                mpq.track('professor view', {'mp_note':"User viewed ${professor}'s profile."});
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
    </style>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Professors</g:link> <span class="divider">/</span></li>
    <li class="active">${professor}</li>
</ul>

<div>
    <div class="details-block professor-block span14">

        <g:if test="${professor.photoUrl}">
            <div class="span3 photo">
                <img src="${professor.photoUrl}" alt="${professor}" title="${professor}">
            </div>
        </g:if>

        <div class="span9 info">
            <h1>${professor}</h1>

            <h3>${professor.title}</h3>

            <div id="statusHolder"></div>

            <g:if test="${professor.email}">
                <div><b>E-mail:</b> <a href="mailto:${professor.email}">${professor.email}</a></div>
            </g:if>

            <div><b>Web page:</b> ${AppUtils.createKangarooLink(professor)}</div>

            <g:if test="${professor.office}">
                <div><b>Office:</b> ${professor.office}</div>
            </g:if>

            <g:if test="${professor.phone}">
                <div><b>Phone:</b> ${professor.phone}</div>
            </g:if>
        </div>
    </div>
</div>

<g:if test="${professor.coursesTeaching.size() > 0}">
    <div class="details-block courses-block span5">

        <div>
            <h3>${professor} is teaching:</h3>

            <b>${Term.findByShortCode("11FA").fullDescription}</b>
            <ul>
                <g:each in="${professor.coursesTeaching.findAll {it.term == Term.findByShortCode('11FA')}}"
                        var="course">
                    <li><g:link controller="course" action="show"
                                id="${course.id}">${course}</g:link> (${course.sectionString()})</li>
                </g:each>
            </ul>


            <b>${Term.findByShortCode("12SP").fullDescription}</b>
            <ul>
                <g:each in="${professor.coursesTeaching.findAll {it.term == Term.findByShortCode('12SP')}}"
                        var="course">
                    <li><g:link controller="course" action="show"
                                id="${course.id}">${course}</g:link> (${course.sectionString()})</li>
                </g:each>
            </ul>
        </div>
    </div>
</g:if>

<g:if test="${professor.colleagues.size() > 0}">
    <div class="details-block courses-block">

        <div>
            <h3>Colleagues:</h3>

            <ul>
                <g:each in="${professor.colleagues}" var="colleague">
                    <li><g:link controller="professor" action="show"
                                id="${colleague.id}">${colleague}</g:link> (${colleague.activeDepartments.join(", ")})</li>
                </g:each>
            </ul>
        </div>
    </div>
</g:if>

<g:if test="${professor.coursesTeaching.size() > 0}">
    <div class="details-block courses-block span14">

        <div>
            <h3>Schedule</h3>

            <div id="calendar"></div>
        </div>
    </div>
</g:if>
</body>
</html>