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
        <a href="#" id="reimportClasses">Run</a>
    </div>
</div>

<div id="facultyImport" class="importBar">
    <b>Faculty:</b>
    <span class="status">${facultyDetails.numFaculty} imported; ${facultyDetails.numMatched} matched (${facultyDetails.percentMatched}%)</span>
    <div class="actionLinks">
        <a href="#" id="rematchFaculty">Run</a>
    </div>
</div>

<div id="textbookImport" class="importBar">
    <b>bkstr.com:</b>
    <span class="status">${textbookDetails.numTextbooks} textbooks; ${textbookDetails.percentCoursesWithBooks}% courses have books.</span>
    <div class="actionLinks">
        <a href="#" id="fetchTextbooks">Run</a>
    </div>
</div>


<div id="amazonImport" class="importBar">
    <b>amazon.com:</b>
    <span class="status">${textbookDetails.numLookedUp} books have Amazon details (${textbookDetails.percentLookedUp}%)
    <div class="actionLinks">
        <a href="#" id="fetchAmazonDetails">Run</a>
    </div>
</div>




</body>
</html>