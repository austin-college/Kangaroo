$.fn.dataTableExt.oStdClasses.sSortAsc = "headerSortDown";
$.fn.dataTableExt.oStdClasses.sSortDesc = "headerSortUp";

var tableHtml;

$(document).ready(function() {
    $("#tableSearch").focus();
    $("#termSelector").change(function() {
        destroyTable();
        getTableData($("#termSelector").val());
    });
    getTableData($("#termSelector").val());
});

function destroyTable() {
    $('#classTable').dataTable().fnDestroy();
    $('#classTable').remove();
    $('#tableHolder').html(tableHtml)
}


function setupTable(data) {
    $('#classTable').dataTable({
        "bProcessing": true,
        "aaData": data.aaData,
        "aaSorting": [
            [1,'asc'] // Sort by YTM.
        ],
        "sPaginationType": "full_numbers",
        "sDom": '<"H"r>t<"F"ip>',
        "oLanguage": {
            "sLengthMenu": "Show _MENU_ classes",
            "sZeroRecords": "<i>Sorry, no courses like that were found.</i>",
            "sInfo": "Showing _START_ to _END_ of _TOTAL_ classes",
            "sInfoEmpty": "",
            "sInfoFiltered": "(instantly filtered from _MAX_)",
            "sSearch":"Search for anything:"
        },
        "iDisplayLength": 25
    });

    $("#tableSearch").live('keyup', function() {
        $('#classTable').dataTable().fnFilter($("#tableSearch").val());
    });

    $('#classTable').dataTable().fnFilter($("#tableSearch").val());
    $('#classTable').show();
}

function getTableData(term) {
    $.ajax({
        url: contextPath + "/home/getData",
        data: {term: term},
        success: function(response) {
            tableHtml = response.tableHtml;
            $('#tableHolder').html(tableHtml)
            setupTable(response.table);
        }
    });

}