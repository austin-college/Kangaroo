<%@ page import="coursesearch.CourseUtils; java.math.MathContext" contentType="text/html;charset=UTF-8" %>
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
        <div>Taught by ${CourseUtils.getProfessorLinksForClass(course, " and ")} in the ${course.department.name} department.</div>
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

        <g:if test="${course.schedule}">
            <div><b>Schedule:</b> <g:link action="bySchedule" id="${course.schedule}">${course.schedule}</g:link></div>
        </g:if>

        <g:if test="${course.room}">
            <div><b>Room:</b> <g:link action="byRoom" id="${course.room}">${course.room}</g:link></div>
        </g:if>

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

<div class="details-block courses-block span12">
    <div>
        <g:if test="${course.textbooksParsed}">
            <g:if test="${course.textbooks.size() > 0}">

                <h3>Textbooks</h3>

                <g:render collection="${course.textbooks}" var="textbook" template="textbook"/>

                <div style="clear: both; padding-top: 10px">
                    <b>Total: $${(course.textbooks*.bookstoreNewPrice.sum() as Double).round(2)}</b> &middot;
                    <a href="${course.textbookPageUrl()}">see all</a>
                </div>
            </g:if>
            <g:else>

                <h3>This course has no textbooks.</h3>

            </g:else>
        </g:if>
        <g:else>
            <h3><a href="${course.textbookPageUrl()}">View this course's textbooks</a></h3>
        </g:else>
    </div>
</div>

</body>
</html>