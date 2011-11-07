<%@ page import="coursesearch.Professor; coursesearch.Course; coursesearch.CourseUtils; java.math.MathContext" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Batch Control</title>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'import_admin.css')}"/>
    <g:javascript src="batchControl.js"/>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Courses</g:link> <span class="divider">/</span></li>
    <li class="active">Import management</li>
</ul>

<div id="importBars">
</div>

</body>
</html>