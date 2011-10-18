<%@ page import="coursesearch.Professor; coursesearch.Course; coursesearch.CourseUtils; java.math.MathContext" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Batch Control</title>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'import_admin.css')}"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
    <g:javascript src="batchControl.js"/>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Courses</g:link> <span class="divider">/</span></li>
    <li class="active">Import management</li>
</ul>


<div id="courseImport" class="importBar">

    <b>Classes:</b>

    <span class="status">${Course.count()} imported</span>

    <div class="actionLinks">

        <a href="#" id="reimportClasses">Re-import</a>

    </div>
</div>

<div id="facultyImport" class="importBar">

    <b>Faculty:</b>

    <span class="status">${facultyDetails.numFaculty} imported; ${facultyDetails.numMatched} matched (${facultyDetails.percentMatched}%)</span>

    <div class="actionLinks">

        <a href="#" id="rematchFaculty">Re-match</a>

    </div>

</div>

<div id="textbookImport" class="importBar">

    <b>Textbooks:</b>

    <span class="status">${textbookDetails.percentCoursesWithBooks}% courses have books. ${textbookDetails.percentLookedUp}% books have Amazon details</span>

    <div class="actionLinks">

        <a href="#" id="fetchTextbooks">Fetch books</a> &middot;

        <a href="#" id="fetchAmazonDetails">Fetch details</a>

    </div>

</div>

</body>
</html>