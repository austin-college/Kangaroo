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
            $.getJSON(sSource, aoData, function (json) {
                fnCallback(json)
            });
        },
        "sDom": '<"H"rf>t<"F"lip>',
        "oLanguage": {
            "sLengthMenu": "Show _MENU_ classes",
            "sZeroRecords": "Nothing found - sorry",
            "sInfo": "Showing _START_ to _END_ of _TOTAL_ classes",
            "sInfoEmpty": "Showing no classes",
            "sInfoFiltered": "(filtered from _MAX_ total classes)",
            "sSearch":"Search for anything:"
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