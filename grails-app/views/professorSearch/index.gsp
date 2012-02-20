<%@ page import="kangaroo.Department; kangaroo.Term" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:javascript src="searchPage.js"/>
    <less:stylesheet name="professorSearch"/>
</head>

<body>

%{--<div id="searchControls">--}%
%{--<div id="bigFilterBar">--}%
%{--<label for="tableSearch">Search for professors:</label>--}%
%{--<g:textField name="tableSearch"/>--}%
%{--</div>--}%
%{--</div>--}%

<div id="professorList">
    <g:render template="miniCard" collection="${list}" var="professor"/>
</div>

</body>
</html>