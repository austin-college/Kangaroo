<%@ page import="kangaroo.Professor; kangaroo.Department; kangaroo.Term" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <r:require module="professorSearch"/>
</head>

<body>


<div id="professorList">

    <g:each in="${'A'..'Z'}" var="letter">
        <g:set var="profs" value="${Professor.findAllByLastNameIlikeAndIsProfessor("$letter%", true)}"/>
        <g:if test="${profs}">
            <div class="letterGrouping">

                <h1>${letter}</h1>

                <g:render template="miniCard" collection="${profs}" var="professor"/>
            </div>

        </g:if>
    </g:each>

</div>

</body>
</html>