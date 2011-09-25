var table;

$.fn.dataTableExt.oStdClasses.sSortAsc = "headerSortDown";
$.fn.dataTableExt.oStdClasses.sSortDesc = "headerSortUp";

$(document).ready(function() {


    table = $('#classTable').dataTable({
        "bProcessing": true,
        "aaSorting": [
            [0,'asc'] // Sort by YTM.
        ],
        "sAjaxSource": contextPath + "/search/getClassesForTable",
        "fnServerData": function (sSource, aoData, fnCallback) {
            /* Add some extra data to the sender */
            //aoData.push({ "name": "minPayments", "value": $("#numPaymentsSlider").slider("value") });
            $.getJSON(sSource, aoData, function (json) { fnCallback(json) });
        }
    });
});

/*
 * Runs an AJAX query on the server, then populates the table with the results.
 */
function runSearch() {
    $.ajax({
        url: contextPath + "/search/getClassesForTable",
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