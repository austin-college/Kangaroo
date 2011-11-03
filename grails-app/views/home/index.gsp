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



<div id="tableFilterControls">
    <label for="termSelector">Term: </label> <g:select name="termSelector" from="${Term.list()}" optionKey="shortCode"/>
</div>

<div id="coursesTableLoading">

         Loading courses...
</div>

<div id="tableHolder">

</div>
</body>
</html>