//================================================================
//
//  batchControl.js
//  Controls the font-end code for the batch-management control page.
//
//================================================================

$(document).ready(function() {

    $("a#reimportClasses").click(runCourseImport);
    $("a#rematchFaculty").click(runFacultyImport);
    $("a#fetchTextbooks").click(runFacultyImport);

});

function runCourseImport() {
    runJob("#courseImport", "reimportCourses", function(response) {
        setCompletionStatus("#courseImport", response, response.details.numCourses + " classes");
    });
    return false;
}

function runFacultyImport() {
    runJob("#facultyImport", "rematchFaculty", function(response) {
        setCompletionStatus("#facultyImport", response, response.details.numFaculty + " imported; " + response.details.numMatched + " matched (" + response.details.percentMatched + "%)");
    });
    return false;
}

function runTextbookImport() {
  runJob("#textbookImport", "textbookImport", function(response) {
        setCompletionStatus("#facultyImport", response, response.details.numFaculty + " imported; " + response.details.numMatched + " matched (" + response.details.percentMatched + "%)");
    });
    return false;
}

function runAmazonImport() {
  runJob("#amazonImport", "amazonImport", function(response) {
        setCompletionStatus("#facultyImport", response, response.details.numFaculty + " imported; " + response.details.numMatched + " matched (" + response.details.percentMatched + "%)");
    });
    return false;
}

/**
 *
 * @param section
 * @param action
 * @param successResponse
 */
function runJob(section, action, successResponse) {
    setStatusMessage(section, "<i>Working...</i>");

    $.ajax({
        url: contextPath + "/batchControl/" + action,
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