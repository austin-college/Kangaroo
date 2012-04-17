<%@ page import="kangaroo.Department; kangaroo.Term" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:javascript src="searchPage.js"/>
</head>

<body>

%{--<div id="searchControls">--}%
%{--<div id="bigFilterBar">--}%
%{--<label for="tableSearch">Search for professors:</label>--}%
%{--<g:textField name="tableSearch"/>--}%
%{--</div>--}%
%{--</div>--}%

<div id="professorList">

    <g:each in="${'A'..'Z'}" var="letter">
        <g:if test="${listByLetters[letter]}">

            <div class="letterGrouping">

                <h1>${letter}</h1>

                <g:render template="miniCard" collection="${listByLetters[letter]}" var="professor"/>
            </div>

        </g:if>
    </g:each>

</div>

</body>
</html>