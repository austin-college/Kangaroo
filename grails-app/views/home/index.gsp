<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'data_table.css')}"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
    <g:javascript src="jquery.dataTables.js"/>
    <g:javascript src="search.js"/>
</head>

<body>

<table cellpadding="0" cellspacing="0" border="0" class="display zebra-striped" id="classTable">
    <thead>
    <tr>
        <th width="8%" class="blue header">Name</th>
        <th width="1%" class="yellow header">Department</th>
        <th width="3%" class="green header">Professor</th>
        <th width="5%" class="header">Schedule</th>
    </tr>
    </thead>
    <tbody></tbody>
    <tfoot>
    <tr>
        <th width="6%">Name</th>
        <th width="1%">Department</th>
        <th width="3%">Professor</th>
        <th width="5%">Schedule</th>
    </tr>
    </tfoot>
</table>

<div id="tableJson" style="display: none;">${tableJson.encodeAsHTML()}</div>
</body>
</html>