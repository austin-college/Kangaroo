<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${professor}</title>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'data_table.css')}"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
    <g:javascript src="jquery.dataTables.js"/>
    <g:javascript src="search.js"/>
</head>

<body>

<ul class="breadcrumb">
  <li><g:link controller="home">Professors</g:link> <span class="divider">/</span></li>
  <li class="active">${professor}</li>
</ul>

<h1>${professor}</h1>
<div>Teaching ${classLinks}.</div>



</body>
</html>