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

                runJob("#facultyImport", "rematchFaculty", function(response) {
                    setCompletionStatus("#facultyImport", response, response.details.numFaculty + " imported; " + response.details.numMatched + " matched (" + response.details.percentMatched + "%)");
                });
                return false;
            });

        });

        /**
         *
         * @param section
         * @param action
         * @param successResponse
         */
        function runJob(section, action, successResponse) {
            setStatusMessage("#facultyImport", "<i>Working...</i>");

            $.ajax({
                url: contextPath + "/batchControl/rematchFaculty",
                success: function(response) {
                    if (response.success)
                        successResponse(response);
                }
            });

        }

        /**
         * Sets the text of the status bar for the given job, with a fade-in animation.
         */
        function setStatusMessage(section, html) {
            $(section + " .status").hide();
            $(section + " .status").html(html);
            $(section + " .status").fadeIn(100);
        }

        /**
         * A shorthand to set the status of a completed job.
         */
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

</body>
</html>