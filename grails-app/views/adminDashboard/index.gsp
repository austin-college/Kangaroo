<%@ page import="kangaroo.data.DataExportService; kangaroo.admin.AdminDataController" %>
<html>
<head>
    <title>Administration</title>
    <meta name="layout" content="main"/>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Kangaroo</g:link> <span class="divider">/</span></li>
    <li class="active">Administration</li>
</ul>

<h2>Kangaroo Administration</h2>

<g:link controller="adminJob">Batch jobs...</g:link>
<g:link controller="adminData">Data export...</g:link>

</body>
</html>