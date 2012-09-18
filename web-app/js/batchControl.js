//================================================================
//
//  batchControl.js
//
//  Controls the front-end code for the "batch jobs" admin page.
//
//================================================================

$(document).ready(function () {

    $.ajax({
        url:contextPath + "/admin/jobs/getJobList",
        success:function (response) {
            $.each(response, function (key, value) {
                addJob(key, value);
            });
        }
    });


    $("a.runLink").live('click', function () {
        var job = $(this).attr('rel');
        var dom = $(this).parent().parent();
        setStatusMessage(dom, "<i>Working...</i>");

        $.ajax({
            url:contextPath + "/admin/jobs/runJob",
            data:{job:job},
            success:function (response) {
                setStatusMessage(dom, getCompletionStatus(response));
            }
        });

        return false;
    });

});

function addJob(name, job) {
    $("#batchJobs").append($(job.html));
}

/**
 * Sets the text of the status bar for the given job, with a fade-in animation.
 */
function setStatusMessage(dom, html) {
    dom.children(".status").hide();
    dom.children(".status").html(html);
    dom.children(".status").fadeIn(100);
}

/**
 * A shorthand to set the status of a completed job.
 */
function getCompletionStatus(response) {
    return response.details.status + "<span class='time'>in " + response.time + "s</span>";
}