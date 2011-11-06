$.fn.dataTableExt.oStdClasses.sSortAsc = "headerSortDown";
$.fn.dataTableExt.oStdClasses.sSortDesc = "headerSortUp";

var terms = {
    "11FA" : "Fall 2011",
    "12SP" : "Spring 2012"
};

var departments = {};

var tableHtml;

$(document).ready(function() {

    departments = $.parseJSON($("#departmentsJson").text());

    $("#selectTermLink").contextMenu({ menu: 'myMenu', leftButton: true }, contextMenuWork);
    $("#selectDepartmentLink").contextMenu({ menu: 'departmentMenu', leftButton: true }, contextMenuWork);

    $("#tableSearch").focus();
    setupTable(tableRaw);
});

function contextMenuWork(action, el, pos) {

    if ($(el).attr('id') == "selectTermLink") {
        $("#selectTermLink").text(terms[action]);
        getTableData(action);
    }
    else if ($(el).attr('id') == "selectDepartmentLink") {

        $("#selectDepartmentLink").text(departments[action]);
//        destroyTable();
//        getTableData(action);
    }
}

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
            destroyTable();
            $('#tableHolder').html(tableHtml)
            setupTable(response.table);
        }
    });
}