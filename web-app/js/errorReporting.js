//==========================================================================
//
// errorReporting.js
//
// JavaScript for the error page.
//
//==========================================================================


function initErrorReporting() {
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


$(document).ready(function () {
    initErrorReporting();
});