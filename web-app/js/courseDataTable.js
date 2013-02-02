//==========================================================================
//
// courseDataTable.js
//
// Configures the DataTables table for the course table.
//
//==========================================================================

// The table.
var objTable;

// Make DataTables use Bootstrap's sorting indicators.
$.fn.dataTableExt.oStdClasses.sSortAsc = "headerSortDown";
$.fn.dataTableExt.oStdClasses.sSortDesc = "headerSortUp";

/**
 * Sets up the table to use the given set of courses. (Re-creates it if it already exists)
 */
function setupTable(data, originalHtml) {

    // If the table already exists on the page, drop it first before recreating. [PC] @todo This is a bit sloppy.
    if (objTable)
        destroyTable(originalHtml);

    objTable = $('#classTable').dataTable({

        // Use the given dataset locally.
        "bProcessing":true,
        "aaData":data.aaData,
        "aaSorting":[
            [1, 'asc'] // Sort by department, initially.
        ],

        // Show real pagination buttons.
        "sPaginationType":"full_numbers",

        // Remove DataTable's default search box.
        "sDom":'<"H"r>t<"F"ip>',
        "bStateSave":true,

        // Customize language strings.
        "oLanguage":{
            "sLengthMenu":"Show _MENU_ classes",
            "sZeroRecords":"<i>Sorry, no courses like that were found.</i>",
            "sInfo":"Showing _START_ to _END_ of _TOTAL_ classes",
            "sInfoEmpty":"",
            "sInfoFiltered":"(instantly filtered from _MAX_)",
            "sSearch":"Search for anything:"
        },
        "iDisplayLength":25,
        "iDisplayStart":parseInt(getPaginationStatus()),
        "fnInitComplete":function (oSettings, json) {

            $('#classTable').show();

            // Load the saved filter string.
            var filterText = getSavedSearch();
            $("#tableSearch").val(filterText);
            this.fnFilter(filterText);

            // Load the save pagination settings...BUT not if there's text in the search string. (see bug below)
            // [PC] @todo do sanity check to see if this page even exists. Paging to a nonexistant page makes the table look empty..
            if (filterText.length == 0)
                this.fnDisplayStart(parseInt(getPaginationStatus()));

            // Whenever the search string is changed, refilter the table.
            $("#tableSearch").live('keyup', function () {
                objTable.fnFilter($("#tableSearch").val());
                saveSearchStatus();
            });
        }
    });

    // Whenever the user changes the pagination, save it to a cookie.
    // [PC] @todo broken, see below.
    $("#classTable_paginate, .paginate_button").live('click', savePaginationStatus);
}

/**
 * Completely resets the table.
 */
function destroyTable(html) {
    objTable.fnDestroy();
    $('#classTable').remove();
    $('#tableHolder').html(html)
}


/**
 * Saves the current search string to a cookie.
 */
function saveSearchStatus() {
    setCookie('table_search', $("#tableSearch").val(), 365);
}

/**
 * Saves pagination status to a cookie.
 */
function savePaginationStatus() {
    // @todo Fix this; objTable is coming up null for some reason when called from a live() event.
    // setCookie('table_displayStart', objTable.oSettings._iDisplayStart, 365);
}

/**
 * Retrieves the last used pagination page from a cookie (if present; otherwise returns the default).
 */
function getPaginationStatus() {

    var cookie = getCookie('table_displayStart');
    if (cookie)
        return cookie;
    else
        return 0;
}

/**
 * Retrieves the last used search string from a cookie (if present; otherwise returns the default).
 */
function getSavedSearch() {

    var cookie = getCookie('table_search');
    if (cookie)
        return cookie;
    else
        return "";
}

/*
 * DATATABLES PLUG-IN
 * ==================
 *
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
