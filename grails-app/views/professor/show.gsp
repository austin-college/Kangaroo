<%@ page import="coursesearch.CourseUtils; coursesearch.Course" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${professor}</title>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'data_table.css')}"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
    <g:javascript src="jquery.dataTables.js"/>
    <g:javascript src="search.js"/>
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
    <div class="details-block courses-block span8">

        <div>
            <h3>Schedule:</h3>

            <g:each in="${schedule.keySet()}" var="day">
                <b>${day}</b>
                <ul>
                    <g:each in="${schedule[day]}" var="scheduleItem">
                        <li>${scheduleItem.time} <g:link controller="course" action="show"
                                    id="${scheduleItem.course.id}">${scheduleItem.course}</g:link>
                        </li>
                    </g:each>
                </ul>
            </g:each>
        </div>
    </div>
</g:if>
</body>
</html>