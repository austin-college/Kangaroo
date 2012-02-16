//==========================================================================
//
// errorReporting.js
//
// JavaScript for the error pages (development and production).
//
//==========================================================================

/**
 * Initializes the "development" error page.
 */
function initDevelopmentErrorReporting() {

    $("#submitExtraInformation").click(submitBugReportDevelopment);
    $("#extraInfo").fadeIn(100);
}

/**
 * Initializes the "production" error page.
 */
function initProductionErrorReporting() {

    $("#submitExtraInformation").click(submitFollowup);

    // Expand/collapse the "error details" panel on the production page.
    $("#showDetails").click(toggleDetailsPanel);

    submitBugReport();

    $("#errorDetailsContainer").hide();
    $("#submitStatus").fadeIn(100);
    $("#extraInfo").fadeIn(100);
}

/**
 * Expands or collapses the "error details" panel on the production page.
 */
function toggleDetailsPanel() {

    var detailsVisible = $("#errorDetailsContainer").is(':visible');
    if (detailsVisible) {
        $("#showDetails").html("show details &raquo;");
        $("#errorDetailsContainer").slideUp(900);

        $('html, body').animate({scrollTop:0}, 950);
    }
    else {
        $("#showDetails").html("&laquo; Hide details");
        $("#errorDetailsContainer").slideDown(900);

        // Scroll to the details.
        $('html, body').animate({scrollTop:$("#errorDetailsContainer").position().top}, 950);
    }
    return false;
}

/**
 * Submits the bug report (called automatically on the production page).
 */
function submitBugReport() {

    $.ajax({
        cache:false,
        dataType:'json',
        type:'POST',
        url:contextPath + '/error/reportBug',
        data:{"sourceUri":sourceUrl, "browser":getBrowserInfo() },
        error:function (response) {
            $("#submitStatus").text("Kangaroo failed to submit the bug report.");
        },
        success:function (response) {
            $("#submitStatus #progress").slideUp();
            $("#submitStatus .header").html("<b>Have no fear!</b> Our development team has been notified.");

            // If the scout message was sent back, display it...
            if (response.message) {
                $("#scoutResponse #message").html(response.message);
                $("#scoutResponse").fadeIn();
                $("#submitStatus").fadeOut();
            }
        }
    });
}

/**
 * Submits the user's followup (called manually on the production page).
 */
function submitFollowup() {

    $.ajax({
        cache:false,
        dataType:'json',
        type:'POST',
        url:contextPath + '/error/addBugDetails',
        data:{"email":$("#userEmail").val(), "reportDetails":$("#reportDetails").val() },
        error:function (response) {
            $("#addReportSubheader").text("Kangaroo failed to submit the followup.");
        },
        success:function (response) {
            $("#addDetailFields").slideUp();
            $("#extraInfo h3").text("Thanks, we've received your details.");
            $("#extraInfo h3").css("text-align", "center");
        }
    });
}

/**
 * Submits both the error report and the details (called manually on the development page).
 */
function submitBugReportDevelopment() {

    $.ajax({
        cache:false,
        dataType:'json',
        type:'POST',
        url:contextPath + '/error/reportBug',
        data:{"sourceUri":sourceUrl, "browser":getBrowserInfo(), "reportDetails":$("#reportDetails").val() },
        error:function (response) {
            ajaxInternalServerError(response, "Kangaroo failed to submit the bug report.");
            $("#extraInfo h3").text("Kangaroo failed to submit the bug report.");

        },
        success:function (response) {
            $("#addDetailFields").slideUp();
            $("#extraInfo h3").text("Report successfully sent to FogBugz!");
            $("#addReportSubheader").text(response.message);
        }
    });
}

/**
 * Returns the user's browser & version.
 */
function getBrowserInfo() {
    var browser;
    if ($.browser.mozilla)
        browser = "Firefox";
    else if ($.browser.msie)
        browser = "Internet Explorer";
    else if ($.browser.opera)
        browser = "Opera";
    else if ($.browser.webkit)
        browser = "Chrome/Safari";
    else
        browser = "Unknown";

    return browser + " " + $.browser.version;
}

function ajaxInternalServerError(response, message) {

    // Replace the whole document with the 500 error page.
    document.write(response.responseText);

    setTimeout(function () {
        // $(document).ready() scripts must be invoked manually, however.
        initErrorReporting();
        initStyling();

        // Add the details to the report body.
        $("#reportDetails").val(message);
    }, 200);
}