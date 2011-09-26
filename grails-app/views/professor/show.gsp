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


<g:if test="${professor.matched}">
    <div class="details-block professor-block">
        <img src="${professor.photoUrl}" alt="${professor}" title="${professor}">

        <div class="info">
            <h1>${professor}</h1>

            <h3>${professor.title}</h3>

            <g:if test="${professor.email}">
                <div><b>E-mail:</b> <a href="mailto:${professor.email}">${professor.email}</a></div>
            </g:if>

            <g:if test="${professor.office}">
                <div><b>Office:</b> ${professor.office}</div>
            </g:if>

            <g:if test="${professor.phone}">
                <div><b>Phone:</b> ${professor.phone}</div>
            </g:if>

            <div>Teaching ${classLinks}.</div>

        </div>
    </div>
</g:if>

</body>
</html>