<%@ page import="kangaroo.AppUtils; kangaroo.AppUtils; java.math.MathContext" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${schedule}</title>
    <meta name="layout" content="main"/>
    <less:stylesheet name="profiles"/>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Courses</g:link> <span class="divider">/</span></li>
    <li class="active">Courses taught at ${schedule}</li>
</ul>

<h2>Courses taught at ${schedule}</h2>

<br/>

<g:each in="${courses.keySet()}" var="department">

    <div><h3>${department}</h3>
        <g:each in="${courses[department]}" var="course">
            <span class="details-block courses-block">
                <div><g:link action="show" id="${course.id}">${course}</g:link></div>

                <div>
                    Taught by ${AppUtils.getProfessorLinksForClass(course, true)}
                </div>

            </span>
        </g:each>
    </div>
</g:each>

</body>
</html>