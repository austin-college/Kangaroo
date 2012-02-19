<%@ page import="kangaroo.Professor; kangaroo.Course; kangaroo.CourseUtils; java.math.MathContext" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Batch Control</title>
    <meta name="layout" content="main"/>
    <less:stylesheet media="screen, print" name="admin"/>
    <less:scripts/>
    <g:javascript src="batchControl.js"/>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Kangaroo</g:link> <span class="divider">/</span></li>
    <li class="active">Batch jobs</li>
</ul>

<div id="batchJobs">
</div>

</body>
</html>