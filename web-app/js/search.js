$.fn.dataTableExt.oStdClasses.sSortAsc = "headerSortDown";
$.fn.dataTableExt.oStdClasses.sSortDesc = "headerSortUp";

var tableHtml;

$(document).ready(function() {
    $("#tableSearch").focus();
    getTableData("11FA");
    $("#termSelector").change(function() {
        destroyTable();
        getTableData($("#termSelector").val());
    });
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
        "sDom": '<"H"r>t<"F"lip>',
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

    $("#tableSearch").keyup(function() {
        $('#classTable').dataTable().fnFilter($("#tableSearch").val());
    });

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