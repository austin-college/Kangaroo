<%@ page import="coursesearch.Term; coursesearch.CourseUtils; coursesearch.Course" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${professor.firstName}: All done!</title>
    <meta name="layout" content="main"/>

    <style type="text/css">
    .profileLink {
        display: inline-block;
        font-weight: bold;
        padding: 10px 10px;
        margin: 15px 0px;
        border: 1px dotted #ddd;
    }
    </style>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Professors</g:link> <span class="divider">/</span></li>
    <li class="active">${professor}</li>
</ul>

<div class="details-block professor-block span14">

    <g:if test="${professor.photoUrl}">
        <div class="span1" style="margin-right: 25px; padding-top: 10px">
            <img src="${resource(dir: 'images', file: 'tick.png')}"/>
        </div>
    </g:if>


    <div class="span12 info">
        <h1>All done, ${professor.firstName}!</h1>

        <div>Your office hours have been saved.</div>
        <br/>
        <br/>

        <div><b>Calendar:</b> Need a printed calendar for your office door? We can create <g:link
                action="printWeeklyCalendar" id="${professor.id}">one with all of your classes and office hours</g:link> on it, in addition to
            your  e-mail. Takes 5 seconds. <g:link action="printWeeklyCalendar" id="${professor.id}">Try it out!</g:link></div>

        <br/>

        <div><b>Web page:</b> Check out <g:link action="show" id="${professor.id}" target="_blank">your shiny Kangaroo page</g:link>.</div>

        <br/>

        <div><b>Syllabi:</b> You can put a link to <g:link action="show" id="${professor.id}" target="_blank">your Kangaroo page</g:link> in your syllabi, and students will always
        be able to easily find your contact details and office hours. Just copy and paste this link:</div>

        <div>


            <span class="profileLink">


                <div><a href="http://kangaroo.austincollege.edu/${professor.id}">kangaroo.austincollege.edu/${professor.id}</a></div>
                %{--<div><a href="${g.createLink(action: 'show', id: professor.id)}"><g:createLink action="show" id="${professor.id}" absolute="true"/></a></div></div>--}%
            </span>
        </div>
    </div>
</div>

</body>
</html>