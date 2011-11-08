$.fn.dataTableExt.oStdClasses.sSortAsc = "headerSortDown";
$.fn.dataTableExt.oStdClasses.sSortDesc = "headerSortUp";

/*
 * Function: fnDisplayStart
 * Purpose:  Set the display start point
 * Returns:  void
 * Inputs:   object:oSettings - DataTables settings object - NOTE - added automatically
 *           int:iStart - New display start point
 *           bool:bRedraw - Redraw the display based on new start point - optional - default true
 */
$.fn.dataTableExt.oApi.fnDisplayStart = function (oSettings, iStart, bRedraw) {
    if (typeof bRedraw == 'undefined')
        bRedraw = true;

    oSettings._iDisplayStart = iStart;
    oSettings.oApi._fnCalculateEnd(oSettings);

    if (bRedraw)
        oSettings.oApi._fnDraw(oSettings);
}

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
    }
}

function destroyTable() {
    $('#classTable').dataTable().fnDestroy();
    $('#classTable').remove();
    $('#tableHolder').html(tableHtml)
}

function setupTable(data) {
    var oTable = $('#classTable').dataTable({
        "bProcessing": true,
        "aaData": data.aaData,
        "aaSorting": [
            [1,'asc'] // Sort by YTM.
        ],
        "sPaginationType": "full_numbers",
        "sDom": '<"H"r>t<"F"ip>',
        "bStateSave": true,
        "oLanguage": {
            "sLengthMenu": "Show _MENU_ classes",
            "sZeroRecords": "<i>Sorry, no courses like that were found.</i>",
            "sInfo": "Showing _START_ to _END_ of _TOTAL_ classes",
            "sInfoEmpty": "",
            "sInfoFiltered": "(instantly filtered from _MAX_)",
            "sSearch":"Search for anything:"
        },
        "iDisplayLength": 25,
        "iDisplayStart": parseInt(getPaginationStatus())
    });


    $("#tableSearch").live('keyup', function() {
        $('#classTable').dataTable().fnFilter($("#tableSearch").val());
    });

    $("#classTable_paginate, .paginate_button").live('click', savePaginationStatus);
    oTable.fnFilter($("#tableSearch").val());

    $('#classTable').show();
    oTable.fnDisplayStart(parseInt(getPaginationStatus()));
}

function savePaginationStatus() {

    var oSettings = $('#classTable').dataTable().fnSettings();
    setCookie('table_displayStart', oSettings._iDisplayStart, 365);
}

function getPaginationStatus() {

    var cookie = getCookie('table_displayStart');
    if (cookie)
        return cookie;
    else
        return 0;
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