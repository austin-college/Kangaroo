<%@ page import="kangaroo.Department; kangaroo.Term" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'data_table.css')}"/>
    <r:require modules="search"/>
</head>

<body>

<div id="searchControls">
    <div id="bigFilterBar">
        <label for="tableSearch">Search for classes:</label>
        <g:textField name="tableSearch"/>
    </div>

    <div id="searchDescription">
        (or professors, majors, meeting times...)
    </div>
</div>

<div class="otherControls">
    Show classes from <a href="#" class="selectable" id="selectTermLink">Spring 2013</a>

    in <a href="#" class="selectable" id="selectDepartmentLink">any department</a>

    %{--that meet at <a href="#" class="selectable">any time</a>--}%
    %{--and that satisfy <a href="#" class="selectable">any requirement</a>.--}%

</div>

<div id="tableHolder">
    <g:render template="emptyTable"/>
</div>

<div id="menus">
    <ul id="termMenu" class="contextMenu">
        <g:each in="${Term.list()}" var="term">
            <li><a href="#${term.id}">${term}</a></li>
        </g:each>
    </ul>

    <ul id="departmentMenu" class="contextMenu">
        <li class=""><a href="#any">(any)</a></li>
        <g:each in="${Department.list()}" var="department">
            <li class="${department.id}"><a href="#${department.id}">${department}</a></li>
        </g:each>
    </ul>
</div>

</body>
</html>