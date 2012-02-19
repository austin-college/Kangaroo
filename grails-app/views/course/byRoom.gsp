<%@ page import="kangaroo.CourseUtils; java.math.MathContext" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${room}</title>
    <meta name="layout" content="main"/>
    <script type="text/javascript">
        $(document).ready(function () {
            mpq.track('room search', {'mp_note':"User viewed courses meeting in ${room}"});
        });
    </script>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Courses</g:link> <span class="divider">/</span></li>
    <li class="active">Courses taught in ${room}</li>
</ul>

<h2>Courses taught in ${room}</h2>

<br/>

<g:each in="${courses.keySet()}" var="department">

    <div><h3>${department}</h3>
        <g:each in="${courses[department]}" var="course">
            <span class="details-block courses-block">
                <div><g:link action="show" id="${course.id}">${course}</g:link></div>

                <div>
                    Taught by ${CourseUtils.getProfessorLinksForClass(course, true)}
                </div>

                <div style="color: #777">
                    ${CourseUtils.getScheduleLinksForClass(course)}
                </div>

            </span>
        </g:each>
    </div>
</g:each>

</body>
</html>