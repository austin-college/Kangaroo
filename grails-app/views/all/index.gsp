<%@ page import="kangaroo.Course; kangaroo.Professor; kangaroo.AppUtils; java.math.MathContext" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Everything</title>
    <meta name="layout" content="main"/>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Courses</g:link> <span class="divider">/</span></li>
    <li class="active">Index</li>
</ul>


<h2>Professors</h2>

<ul>
    <g:each in="${Professor.list()}" var="professor">
        <li><g:link controller="professor" action="show" id="${professor.id}">${professor}</g:link></li>
    </g:each>
</ul>


<h2>Classes</h2>

<ul>
    <g:each in="${Course.list()}" var="course">
        <li><g:link controller="course" action="show" id="${course.id}">${course}</g:link></li>
    </g:each>
</ul>

</body>
</html>