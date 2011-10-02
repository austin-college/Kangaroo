<%@ page import="java.math.MathContext" contentType="text/html;charset=UTF-8" %>
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

<div>
    <div class="details-block">
        <div>Taught by ${profLinks} in the ${course.department.name} department.</div>
    </div>
</div>

<div class="details-block courses-block span6">
    <h3>Registration info</h3>

    <g:if test="${course.instructorConsentRequired}">
        <div class="alert-message block-message info">
            This course requires instructor permission to register.
        </div>
    </g:if>

    <div><b>Section:</b> ${course.sectionString()}</div>

    <div><b>ZAP:</b> ${course.zap}</div>

    <g:if test="${course.comments}">
        <div><b>Requirements:</b> ${course.comments}</div>
    </g:if>
</div>

<div class="details-block courses-block">

    <div>
        <h3>Meeting info</h3>

        <div><b>Schedule:</b> ${course.schedule}</div>

        <div><b>Room:</b> ${course.room}</div>

    </div>
</div>

<div class="details-block courses-block span6">
    <div>
        <h3>Size</h3>

        <div><b>${course.seatsUsed}</b> students out of a possible <b>${course.capacity}</b> <span
                style="color: ${fullPercentageColor}">(${fullPercentage}% full)</span>
        </div>
    </div>
</div>

</body>
</html>