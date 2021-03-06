<%@ page import="kangaroo.AppUtils; kangaroo.Professor; kangaroo.Term; kangaroo.AppUtils; kangaroo.Course" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${professor}</title>
    <meta name="layout" content="main"/>

    <script type="text/javascript">
        var professorId = "${professor.id}";
    </script>

    <r:require modules="professor"/>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="professorSearch">Professors</g:link> <span class="divider">/</span></li>
    <li class="active">${professor}</li>
</ul>

<bootstrap:flashMessages/>

<g:if test="${!professor.isActive}">
    <bootstrap:blockMessage type="warning" message="This professor is no longer teaching at Austin College."/>
</g:if>

<div id="professor">
    <g:render template="infoBlock" var="professor"/>

    <g:if test="${professor.isActive}">
        <g:render template="schedule" var="professor"/>
        <g:render template="coursesTeaching" var="professor"/>
        <g:render template="colleagues" var="professor"/>
    </g:if>
</div>

</body>
</html>