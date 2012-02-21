//==========================================================================
//
// searchPage.js
//
// JavaScript for the main course search page.
// @todo [PC] Rewrite to do ALL processing (ie, term switching) client-side.
//
//==========================================================================

var terms = {
    "11FA":"Fall 2011",
    "12SP":"Spring 2012"
};

var departments = {};
var originalTableHtml;

$(document).ready(function () {

    // Read in the data stored in the page.
    originalTableHtml = $("#tableHolder").html();
    departments = $.parseJSON($("#departmentsJson").text());

    // Create context menus for the term and department links.
    $("#selectTermLink").contextMenu({ menu:'myMenu', leftButton:true }, contextMenuClicked);
    $("#selectDepartmentLink").contextMenu({ menu:'departmentMenu', leftButton:true }, contextMenuClicked);

    // Set up the table!
    $("#tableSearch").focus();
    setupTable(tableRaw);
});

/**
 * Fetches the table data for the given term.
 */
function getTableData(term) {
    $.ajax({
        url:contextPath + "/home/getData",
        data:{term:term},
        success:function (response) {
            setupTable({aaData:response.table.aaData}, originalTableHtml);
        }
    });
}

/**
 * Run when an item is clicked in the context menu.
 */
function contextMenuClicked(action, el, pos) {

    if ($(el).attr('id') == "selectTermLink") {

        // Fetch data for this term and reload the table.
        $("#selectTermLink").text(terms[action]);
        getTableData(action);
    }
    else if ($(el).attr('id') == "selectDepartmentLink") {

        // If they selected "any department", show them all.
        if (action == "any") {
            $("#selectDepartmentLink").text("any department");
            setupTable(tableRaw, originalTableHtml);
        }
        else {
            $("#selectDepartmentLink").text(departments[action]);

            // Filter the list by this department.
            var newTable = [];
            $.each(tableRaw.aaData, function (i, row) {

                if (row[1] == departments[action])
                    newTable.push(row);
            });

            setupTable({aaData:newTable}, originalTableHtml);
        }
    }
}

