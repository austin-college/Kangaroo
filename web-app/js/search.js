$.fn.dataTableExt.oStdClasses.sSortAsc = "headerSortDown";
$.fn.dataTableExt.oStdClasses.sSortDesc = "headerSortUp";

$(document).ready(function() {
    getTableData("11FA");
});

function setupTable(data) {
    $('#classTable').dataTable({
        "bProcessing": true,
        "aaData": data.aaData,
        "aaSorting": [
            [1,'asc'] // Sort by YTM.
        ],
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

    $('#classTable').show();
    $("#tableJson").text('');
    $("#classTable_filter input").focus();
}

function getTableData(term) {
    $.ajax({
        url: contextPath + "/home/getData",
        data: {term: term},
        success: function(response) {
            setupTable(response.table);
        }
    });

}