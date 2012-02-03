<%@ page import="coursesearch.Term; coursesearch.CourseUtils; coursesearch.Course" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${professor.firstName}: Set your office hours</title>
    <meta name="layout" content="main"/>

    <style type="text/css">
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


    <div class="span8 info">
        <h1>All done, ${professor.firstName}!</h1>

        <div>To set your office hours, just <b>click</b> and <b>drag</b> on the calendar.</div>
    </div>
</div>

</body>
</html>