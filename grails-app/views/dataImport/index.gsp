<%@ page import="coursesearch.Professor; coursesearch.Course; coursesearch.CourseUtils; java.math.MathContext" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Data Import</title>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'import_admin.css')}"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Courses</g:link> <span class="divider">/</span></li>
    <li class="active">Data import</li>
</ul>

<g:form action="submit" style="margin-left: 40px">

    <g:if test="${flash.success}">
        ${flash.success}
    </g:if>

    <h3>Paste your JSON here:</h3>

    <div><g:textArea name="json" rows="10" cols="10" style="width: 500px; height: 400px"/></div>

    <div style="margin-top: 20px"><button class="btn large primary">Import!</button></div>
</g:form>

</body>
</html>