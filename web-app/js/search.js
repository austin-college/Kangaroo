$(document).ready(function() {
    $("#courseSearch").focus();
    $("#courseSearch").keyup(runSearch);

});

/*
 * Runs an AJAX query on the server, then populates the table with the results.
 */
function runSearch() {
    $.ajax({
        url: contextPath + "/search/getClassesAjax",
        dataType: 'json',
        data: { s: $("#courseSearch").val() },
        success: function(response) {
            $('#resultsArea').html("");
            $.each(response, function(index, value) {
                $("#resultsArea").append("<h2>" + value.name + "</h2>");
            });
        }
    });
}