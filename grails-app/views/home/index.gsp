<%@ page import="coursesearch.Term" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'data_table.css')}"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
    <g:javascript src="jquery.dataTables.js"/>
    <g:javascript src="search.js"/>
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

<div id="otherControls">
    <label for="termSelector">Term:</label> <g:select name="termSelector" from="${Term.list()}"
                                                      optionKey="shortCode" value="12SP"/>
</div>

<div id="tableHolder">
    <g:render template="emptyTable"/>
</div>
</body>
</html>