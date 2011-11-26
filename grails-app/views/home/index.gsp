<%@ page import="coursesearch.Department; coursesearch.Term" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'data_table.css')}"/>
    <link rel="stylesheet" href="${resource(dir: 'libraries', file: 'contextMenu/jquery.contextMenu.css')}"/>
    <g:javascript src="cookies.js"/>
    <g:javascript src="jquery.dataTables.js"/>
    <g:javascript src="search3.js"/>
    <g:javascript src="../libraries/contextMenu/jquery.contextMenu.js"/>
    <style type="text/css">
    #newStuff {
        font-size: 18px;
        margin: 10px auto 30px auto;
        width: 400px;
        padding: 9px;
        text-align: center;
        background-color: #dfd;
        border: 1px solid #ded;
    }
    </style>
</head>

<body>

<div id="newStuff">
    <b>New!</b> We now have <g:link controller="majors">majors and minors</g:link>!</a>
</div>

<div id="searchControls">
    <div id="tableFilterBar">
        <label for="tableSearch">Search for classes:</label>
        <g:textField name="tableSearch"/>
    </div>

    <div id="searchDescription">
        (or professors, majors, meeting times...)
    </div>
</div>

<div class="otherControls">
    Show classes from <a href="#" class="selectable" id="selectTermLink">Spring 2012</a>

    in <a href="#" class="selectable" id="selectDepartmentLink">any department</a>

    %{--that meet at <a href="#" class="selectable">any time</a>--}%
    %{--and that satisfy <a href="#" class="selectable">any requirement</a>.--}%

</div>

<div id="tableHolder">
    <g:render template="emptyTable"/>
</div>


%{-- CACHED DATA --}%
<div style="display: none">
    <div id="departmentsJson">${departmentsJson.encodeAsHTML()}</div>
</div>

%{-- menus --}%
<ul id="myMenu" class="contextMenu">
    <g:each in="${Term.list()}" var="term">
        <li><a href="#${term.shortCode}">${term}</a></li>
    </g:each>
</ul>

<ul id="departmentMenu" class="contextMenu">
    <li class=""><a href="#any">(any)</a></li>
    <g:each in="${Department.list()}" var="department">
        <li class="${department.code}"><a href="#${department.code}">${department}</a></li>
    </g:each>
</ul>

<script type="text/javascript">
    var tableRaw = ${tableJson};
</script>
</body>
</html>