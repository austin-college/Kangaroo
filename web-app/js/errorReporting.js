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

    $("#submitExtraInformation").attr("disabled", "disabled");
    $("#submitExtraInformation").css({ opacity:0.5 });
    $("#extraInfo").css({ opacity:0.7 });

    $.ajax({
        cache:false,
        dataType:'json',
        type:'POST',
        url:contextPath + '/error/addBugDetails',
        data:{"email":$("#userEmail").val(), "reportDetails":$("#reportDetails").val() },
        error:function (response) {
            followupFailed();
            $("#extraInfo h3").text("Sending failed; please try again.");
        },
        success:function (response) {

            followupSucceeded();
            $("#extraInfo h3").text("Thanks, we've received your details.");
        }
    });
}

/**
 * Submits both the error report and the details (called manually on the development page).
 */
function submitBugReportDevelopment() {

    $("#submitExtraInformation").attr("disabled", true);
    $("#submitExtraInformation").css({ opacity:0.5 });
    $("#extraInfo").css({ opacity:0.7 });

    $.ajax({
        cache:false,
        dataType:'json',
        type:'POST',
        url:contextPath + '/error/reportBug',
        data:{"sourceUri":sourceUrl, "browser":getBrowserInfo(), "reportDetails":$("#reportDetails").val() },
        error:function (response) {
            followupFailed();
            $("#extraInfo h3").text("Sending failed; please try again.");
        },
        success:function (response) {

            followupSucceeded();
            $("#extraInfo h3").text("Report successfully sent to FogBugz!");
        }
    });
}

function followupSucceeded() {
    $("#addDetailFields").slideUp();
    $("#extraInfo").animate({ opacity:1.0 }, 200);
    $("#addReportSubheader").fadeOut();
}

function followupFailed() {
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