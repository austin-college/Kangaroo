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
        var professorId = "${professor.id}";
    </script>
    <g:javascript src="professorCalendar.js"/>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="professorSearch">Professors</g:link> <span class="divider">/</span></li>
    <li class="active">${professor}</li>
</ul>

<g:render template="infoBlock" var="professor"/>
<g:render template="coursesTeaching" var="professor"/>
<g:render template="colleagues" var="professor"/>
<g:render template="schedule" var="professor"/>

</body>
</html>