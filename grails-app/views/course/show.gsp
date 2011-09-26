<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${course}</title>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'data_table.css')}"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
    <g:javascript src="jquery.dataTables.js"/>
    <g:javascript src="search.js"/>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Courses</g:link> <span class="divider">/</span></li>
    <li class="active">${course}</li>
</ul>

<h1>${course}</h1>

<div class="details-block">
    <div>Taught by ${profLinks} in the ${course.department.name} department.</div>

    <div class="details">
        <div><b>Meets:</b> ${course.schedule}</div>
        <div><b>Room:</b> ${course.room}</div>

        <div><b>Section:</b> ${course.sectionString()} (ZAP ${course.zap})</div>
        <g:if test="${course.instructorConsentRequired}">
            <div class="alert-message block-message info">
                This course requires instructor permission to register.
            </div>
        </g:if>
        <g:if test="${course.comments}">
            <div><b>Comments:</b> ${course.comments}</div>
        </g:if>
    </div>

</div>

</body>
</html>