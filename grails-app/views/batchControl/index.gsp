<%@ page import="coursesearch.Professor; coursesearch.Course; coursesearch.CourseUtils; java.math.MathContext" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Batch Control</title>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'import_admin.css')}"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {

            $("a#rematchFaculty").click(function() {

                setStatusMessage("#facultyImport", "<i>Working...</i>");
                $.ajax({
                    url: contextPath + "/batchControl/rematchFaculty",
                    success: function(response) {
                        setCompletionStatus("#facultyImport", response, response.details.numFaculty + " imported; " + response.details.numMatched + " matched (" + response.details.percentMatched + "%)");
                    }
                });

                return false;
            });

        });

        function setStatusMessage(section, html) {
            $(section + " .status").hide();
            $(section + " .status").html(html);
            $(section + " .status").fadeIn(100);
        }

        function setCompletionStatus(section, response, body) {

            var html = body + "<span class='time'>in " + response.time + "s</span>";
            setStatusMessage(section, html);
        }
    </script>
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

        <a href="#">Re-import</a> &middot; <a href="#">Clear</a>

    </div>
</div>

<div id="facultyImport" class="importBar">

    <b>Faculty:</b>

    <span class="status">${facultyDetails.numFaculty} imported; ${facultyDetails.numMatched} matched (${facultyDetails.percentMatched}%)</span>

    <div class="actionLinks">

        <a href="#" id="rematchFaculty">Re-match</a> &middot; <a href="#">Clear</a>

    </div>

</div>

</body>
</html>