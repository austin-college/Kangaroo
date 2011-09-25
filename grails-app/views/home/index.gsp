<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'data_table.css')}"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
    <g:javascript src="jquery.dataTables.min.js"/>
    <g:javascript src="search.js"/>
</head>

<body>

<div id="searchArea">
    <label for="courseSearch">Search for classes:</label> <g:textField name="courseSearch" class="span5 xlarge"/>
</div>

<br/>
<table cellpadding="0" cellspacing="0" border="0" class="display" id="classTable">
    <thead>
    <tr>
        <th width="6%">Name</th>
        <th width="1%">Department</th>
        <th width="3%">Professor</th>
        <th width="1%">Section</th>
        <th width="1%">Available</th>
        <th width="5%">Schedule</th>
    </tr>
    </thead>
    <tbody></tbody>
    <tfoot>
    <tr>
        <th width="6%">Name</th>
        <th width="1%">Department</th>
        <th width="3%">Professor</th>
        <th width="1%">Section</th>
        <th width="1%">Available</th>
        <th width="5%">Schedule</th>
    </tr>
    </tfoot>
</table>

</body>
</html>