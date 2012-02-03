<%@ page import="coursesearch.Term; coursesearch.CourseUtils; coursesearch.Course" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${professor}</title>
    <meta name="layout" content="print"/>
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
                height:450,
                allDaySlot:false,
                minTime:8,
                maxTime:18,
                timeFormat:'',
                loading:function (isLoading, view) {

                    if (!isLoading) {
                        print();
                    }
                }
            });
        });
    </script>
    <style type="text/css">

    table.fc-agenda-slots {
        margin-bottom: 0;
    }
    </style>
</head>

<body>

<div>
    <div class="details-block professor-block" id="professorInfo">

        <g:if test="${professor.photoUrl}">
            <div class="photo" style="margin-right: 20px; float: left">
                <img src="${professor.photoUrl}" alt="${professor}" title="${professor}" width="100px">
            </div>
        </g:if>

        <div class="info">
            <h1>${professor}</h1>

            <h3>${professor.title}</h3>

            <div id="statusHolder"></div>

            <g:if test="${professor.email}">
                <div><b>E-mail:</b> <a href="mailto:${professor.email}">${professor.email}</a></div>
            </g:if>

            <div><b>Web:</b> <a
                    href="http://kangaroo.austincollege.edu/${professor.id}">kangaroo.austincollege.edu/${professor.id}</a>
            </div>

            <g:if test="${professor.office}">
                <div><b>Office:</b> ${professor.office}</div>
            </g:if>

            <g:if test="${professor.phone}">
                <div><b>Phone:</b> ${professor.phone}</div>
            </g:if>
        </div>
    </div>
</div>

<div class="details-block courses-block span16" style="clear: both; margin-top: 60px">

    <div>

        <div id="calendar"></div>
    </div>
</div>
</body>
</html>