<%@ page import="kangaroo.data.DataExportService; kangaroo.admin.AdminDataController" %>
<html>
<head>
    <title>Data Management</title>
    <meta name="layout" content="main"/>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Kangaroo</g:link> <span class="divider">/</span></li>
    <li class="active">Data management</li>
</ul>

<h2>Data Export</h2>

<g:link action="runExport">Download all data via JSON</g:link> (v${DataExportService.formatVersion})

</body>
</html>