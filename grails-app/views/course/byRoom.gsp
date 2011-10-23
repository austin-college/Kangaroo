<%@ page import="coursesearch.CourseUtils; java.math.MathContext" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${room}</title>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'data_table.css')}"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
    <g:javascript src="jquery.dataTables.js"/>
    <g:javascript src="search.js"/>
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
                <div><g:link action="show" id="${course.zap}">${course}</g:link></div>

                <div>
                    Taught by ${CourseUtils.getProfessorLinksForClass(course)}
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