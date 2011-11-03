$.fn.dataTableExt.oStdClasses.sSortAsc = "headerSortDown";
$.fn.dataTableExt.oStdClasses.sSortDesc = "headerSortUp";

$(document).ready(function() {
    getTableData("11FA");
    $("#termSelector").change(function() {
        destroyTable();
        getTableData($("#termSelector").val());
    });
});

function destroyTable() {
    $('#classTable').dataTable().fnDestroy();
    $('#classTable').remove();

}

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
    $("#coursesTableLoading").hide();
}

function getTableData(term) {
    $("#coursesTableLoading").show();
    $.ajax({
        url: contextPath + "/home/getData",
        data: {term: term},
        success: function(response) {
            $("#tableHolder").html(response.tableHtml);
            setupTable(response.table);
        }
    });

}