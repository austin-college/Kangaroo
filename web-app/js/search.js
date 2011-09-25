$.fn.dataTableExt.oStdClasses.sSortAsc = "headerSortDown";
$.fn.dataTableExt.oStdClasses.sSortDesc = "headerSortUp";

$(document).ready(function() {


    $('#classTable').dataTable({
        "bProcessing": true,
        "aaSorting": [
            [1,'asc'] // Sort by YTM.
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
        },
        "iDisplayLength": 15
    });

    $("#classTable_filter input").focus();
});