//==========================================================================
//
// searchPage.js
//
// JavaScript for the main course search page.
// @todo [PC] Rewrite to do ALL processing (ie, term switching) client-side.
//
//==========================================================================

document.Kangaroo.searchPage = {
    emptyTableHtml: "",
    data: {}
};

$(document).ready(function () {
    // Store the empty table HTML so we can recreate it later.
    document.Kangaroo.searchPage.emptyTableHtml = $("#tableHolder").html();

    $("#terms").change(refetchTable);
    $("#departments").change(filterByDepartment);

    // Set up the table!
    $("#tableSearch").focus();
    getTableData(document.Kangaroo.defaultSearchTerm);
});

function setLoadingState(isLoading) {
    if (isLoading) {
        $("#loading").show();
        $("#searchArea").addClass("disabled");
    } else {
        $("#loading").fadeOut();
        $("#searchArea").removeClass("disabled");
    }
}

/**
 * Fetches the table data for the given term.
 */
function getTableData(term) {
    setLoadingState(true);
    $.ajax({
        url: document.Kangaroo.url("/home/getData"),
        data: {term: term},
        success: function (response) {
            setLoadingState(false);
            document.Kangaroo.searchPage.data = response.table;
            setupTable(document.Kangaroo.searchPage.data);
        }
    });
}

/**
 * Re-fetches the table using the current on-page settings.
 */
function refetchTable() {
    getTableData($("#terms").val());
}

/**
 * Re-filters the table using the current on-page settings.
 */
function filterByDepartment() {
    var department = $("#departments").val();

    // If they selected "any department", show them all.
    if (department == "ANY") {
        setupTable(document.Kangaroo.searchPage.data);
    }
    else {
        // Filter the list by this department.
        var newTable = [];
        $.each(document.Kangaroo.searchPage.data.aaData, function (i, row) {

            if (row[1] == document.Kangaroo.departments[department])
                newTable.push(row);
        });

        setupTable({aaData: newTable});
    }
}


