<%@ page import="kangaroo.Course; kangaroo.AppUtils; kangaroo.mn.CourseFulfillsRequirement; kangaroo.AppUtils; java.math.MathContext" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${course}</title>
    <meta name="layout" content="main"/>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">${course.term.fullDescription} Courses</g:link> <span class="divider">/</span></li>
    <li class="active">${course}</li>
</ul>

<div class="details-block span15" style="padding-top: 15px">

    <h1 style="margin-bottom: 8px">
        ${course} <span style="color: #999; font-size: 18px">${course.sectionString()}</span>
    </h1>

    <g:if test="${course.description}">
        <blockquote style="margin-bottom: 10px">
            <p style="font-size: 18px; line-height: 160%">${course.description}</p>
        </blockquote>
    </g:if>

    <div style="margin-bottom: 10px; padding: 5px 0">
        <g:if test="${profImage}">
            <div class="miniPhoto">
                <g:link controller="professor" action="show" id="${course.instructors[0].id}">
                    <img src="${profImage}" title="${course.instructors[0]}" alt="${course.instructors[0]}"
                         width="40px" style="float: left; border: 1px solid #ddd">
                </g:link>
            </div>

        </g:if>
        <div style="margin-left: 53px">
            <div><b>Taught by ${AppUtils.getProfessorLinksForClass(course, false, " and ")}</b></div>

            <div style="font-variant: small-caps">${course.department.name}</div>
        </div>

    </div>
</div>

<div class="details-block span15" style="padding-top: 25px; padding-bottom: 25px">
    <div style="float: left">
        <div style="font-size: 30px; color: #999; font-weight: bold; margin-bottom: 10px">Meets</div>

        <div>
            <g:if test="${course.room}">
                <div>in <g:link action="byRoom" id="${course.room}">${course.room}</g:link></div>
            </g:if>

        %{--<g:if test="${course.schedule}">--}%
            <div>at ${AppUtils.getScheduleLinksForClass(course)}</div>
            %{--</g:if>--}%
        </div>
    </div>

    <div style="float: right; display: inline-block; width: 260px">
        <div style="font-size: 30px; color: #999; font-weight: bold; margin-bottom: 10px; margin-right: 30px">Details</div>

        <div>
            <div>zap: <b>${course.zap}</b></div>

            <div>section: <b>${course.sectionString()}</b></div>
            <g:if test="${course.instructorConsentRequired}">
                <div>
                    <b>instructor consent required</b>
                </div>
            </g:if>
        </div>
    </div>

    <div style="clear: both;"></div>

    <g:if test="${course.hasLabs}">
        <div style="font-size: 30px; color: #999; font-weight: bold; margin-top: 45px; margin-bottom: 10px">Lab Info</div>

        $50 lab fee required. You will also need to register one of the following lab sections:
        <ul style="padding: 5px 0">
            <g:each in="${course.siblings.findAll { it.isLab }}" var="lab">
                <li style="padding: 3px 0">
                    <b><g:link action="show" id="${lab.id}">${lab.sectionString()}</g:link>:</b>
                    ${lab.meetingTimes.join(" and ")}</li>
            </g:each>
        </ul>
    </g:if>

    <div style="float: left; clear: both; margin-top: 40px">
        <div style="font-size: 30px; color: #999; font-weight: bold; margin-bottom: 10px">Textbooks</div>

        <g:if test="${course.textbooksParsed}">
            <g:if test="${course.textbooks.size() > 0}">
                <g:render collection="${course.textbooks}" var="textbook" template="textbook"/>

                <div style="clear: both; padding-top: 10px">
                    <b>Total: $${(course.textbooks*.bookstoreNewPrice.sum() as Double).round(2)}</b> &middot;
                    <a href="${course.textbookPageUrl()}">see all</a>
                </div>
            </g:if>
            <g:else>

                No textbooks have been posted.

            </g:else>
        </g:if>
        <g:else>
            <a href="${course.textbookPageUrl()}">View this course's textbooks</a>
        </g:else>
    </div>

    <g:if test="${course.requirementsFulfilled}">
        <div style="float: right; margin-top: 40px; display: inline-block; width: 260px">
            <div style="font-size: 30px; color: #999; font-weight: bold; margin-bottom: 10px; margin-right: 30px">Fulfills</div>

            <div>
                <g:each in="${course.requirementsFulfilled}" var="requirement">
                    <div class="courseFulfills"><img src="${resource(dir: 'images', file: 'check.png')}" height="16px"
                                                     width="16px" border="0"/> ${requirement} credit</div>
                </g:each>
            </div>
        </div>
    </g:if>


    <g:if test="${course.comments}">
        <div style="float: left; clear: both; margin-top: 40px">
            <div style="font-size: 30px; color: #999; font-weight: bold; margin-bottom: 10px">Notes</div>

            <div style="margin-top: 5px; margin-bottom: 10px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif">
                <p style="font-size: 16px; line-height: 160%">${course.comments}</p>
            </div>
        </div>
    </g:if>
</div>

</body>
</html>