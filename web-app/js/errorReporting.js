//==========================================================================
//
// errorReporting.js
//
// JavaScript for the error pages (development and production).
//
//==========================================================================

var isInProduction = false;

/**
 * Initializes the "development" error page.
 */
function initDevelopmentErrorReporting() {

    isInProduction = false;
    $("#submitExtraInformation").click(submitFollowup);
    $("#extraInfo").fadeIn(100);
}

/**
 * Initializes the "production" error page.
 */
function initProductionErrorReporting() {

    isInProduction = true;
    $("#submitExtraInformation").click(submitFollowup);

    // Expand/collapse the "error details" panel on the production page.
    $("#showDetails").click(toggleDetailsPanel);

    // Auto-submit the error report now.
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
 * Submits the user's followup (called manually on both pages).
 */
function submitFollowup() {

    $("#submitExtraInformation").attr("disabled", "disabled");
    $("#submitExtraInformation").css({ opacity:0.5 });
    $("#extraInfo").css({ opacity:0.7 });

    // In development, this is our first report, not our followup.
    var url = contextPath + '/error/' + (isInProduction ? 'addBugDetails' : 'reportBug');
    var data = {"email":$("#userEmail").val(), "reportDetails":$("#reportDetails").val() };

    if (!isInProduction) { // So add more data in development too.
        data["reportDetails"] = $("#reportDetails").val();
        data["sourceUri"] = sourceUrl;
        data["browser"] = getBrowserInfo();
    }

    // Send the request.
    $.ajax({
        cache:false,
        dataType:'json',
        type:'POST',
        url:url,
        data:data,
        error:followupFailed,
        success:followupSucceeded
    });
}

/**
 * Called when the followup succeeds; closed up the form.
 */
function followupSucceeded() {
    $("#extraInfo h3").text("Thanks, we've received your details.");
    $("#addDetailFields").slideUp();
    $("#extraInfo").animate({ opacity:1.0 }, 200);
    $("#addReportSubheader").fadeOut();
}

/**
 * Called when the followup fails; re-enables the form for another go.
 */
function followupFailed() {
    $("#extraInfo h3").text("Sending failed; please try again.");
    $("#extraInfo").animate({ opacity:1.0 }, 200);
    $("#submitExtraInformation").animate({ opacity:1.0 }, 200);
    $("#submitExtraInformation").attr("disabled", false);
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