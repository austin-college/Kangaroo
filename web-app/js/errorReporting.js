//==========================================================================
//
// errorReporting.js
//
// JavaScript for the error page.
//
//==========================================================================


function initDevelopmentErrorReporting() {
    $("#submitExtraInformation").click(function () {
        $.ajax({
            cache:false,
            dataType:'json',
            type:'POST',
            url:contextPath + '/error/reportBug',
            data:"sourceUri=${request.forwardURI - request.contextPath}" +
                "&browser=" + getBrowserInfo() +
                "&reportDetails=" + $("#reportDetails").val(),
            error:function (response) {
                ajaxInternalServerError(response, "Kangaroo failed to submit the bug report.");
                $("#submitStatus").text("Kangaroo failed to submit the bug report.");

            },
            success:function (response) {
                $("#addDetailFields").slideUp();
                $("#reportHeader").text("Report successful!");
                $("#addReportSubheader").text(response.message);
            }
        });
    });
}

function initProductionErrorReporting() {

    $("#showDetails").click(function () {

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
    });

    $("#submitExtraInformation").click(function () {

        $.ajax({
            cache:false,
            dataType:'json',
            type:'POST',
            url:contextPath + '/error/addBugDetails',
            data:"email=" + $("#userEmail").val() +
                "&reportDetails=" + $("#reportDetails").val(),
            error:function (response) {
                $("#addReportSubheader").text("Kangaroo failed to submit the followup.");
            },
            success:function (response) {
                $("#addDetailFields").slideUp();
                $("#extraInfo h3").text("Thanks, we've received your details.");
                $("#extraInfo h3").css("text-align", "center");
            }
        });
    });

    $.ajax({
        cache:false,
        dataType:'json',
        type:'POST',
        url:contextPath + '/error/reportBug',
        data:"sourceUri=${request.forwardURI - request.contextPath}" +
            "&browser=" + getBrowserInfo(),
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

    $("#errorDetailsContainer").hide();
    $("#submitStatus").fadeIn(100);
    $("#extraInfo").fadeIn(100);
}

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