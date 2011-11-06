<%@ page import="coursesearch.Term" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'data_table.css')}"/>
    <link rel="stylesheet" href="${resource(dir: 'libraries', file: 'contextMenu/jquery.contextMenu.css')}"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
    <g:javascript src="jquery.dataTables.js"/>
    <g:javascript src="search.js"/>
    <g:javascript src="../libraries/contextMenu/jquery.contextMenu.js"/>
</head>

<body>

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

    in <a href="#" class="selectable">any department</a>

    %{--that meet at <a href="#" class="selectable">any time</a>--}%
    and that satisfy <a href="#" class="selectable">any requirement</a>.

</div>

<div id="tableHolder">
    <g:render template="emptyTable"/>
</div>

<!-- Right Click Menu -->
<ul id="myMenu" class="contextMenu">
    <li class="11FA"><a href="#insert">Fall 2011</a></li>
    <li class="12SP"><a href="#insert">Spring 2012</a></li>
</ul>
</body>
</html>