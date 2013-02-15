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
    data: {},
    rows: {}
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
            document.Kangaroo.searchPage.data = response.courses;
            document.Kangaroo.searchPage.table = convertToTable(response.courses);
            setupTable(document.Kangaroo.searchPage.table);
        }
    });
}

function convertToTable(rowObjs) {
    var rows = [];
    $.each(rowObjs, function (i, rowObj) {
        rows.push(convertToRow(rowObj));
    });

    return {
        "aaData": rows,
        "iTotalRecords": rows.length,
        "iTotalDisplayRecords": rows.length
    };
}

function convertToRow(courseArray) {
    var course = {
        id: courseArray[0],
        name: courseArray[1],
        department: courseArray[2],
        professors: $.map(courseArray[3], function (array) {
            return { id: array[0], name: array[1] };
        }),
        meetingTimes: $.map(courseArray[4], function (array) {
            return { id: array[0], name: array[1] };
        })
    };

    var courseName = "<a href='" + document.Kangaroo.url("/course/" + course.id) + "'>" + course.name + "</a>";
    var professorLinks = $.map(course.professors, function (obj) {
        return "<a href='" + document.Kangaroo.url("/" + obj.id) + "'>" + obj.name + "</a>";
    });
    var meetingTimeLinks = $.map(course.meetingTimes, function (obj) {
        return "<a href='" + document.Kangaroo.url("/course/bySchedule/" + obj.id) + "'>" + obj.name + "</a>";
    });

    if (!course.professors.length) {
        professorLinks = "<i>Unknown</i>";
    }
    if (!course.meetingTimes.length) {
        meetingTimeLinks = "<i>Unknown</i>";
    }

    return [courseName, course.department, professorLinks, meetingTimeLinks];
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
        setupTable(document.Kangaroo.searchPage.table);
    }
    else {
        // Filter the list by this department.
        var newTable = [];
        $.each(document.Kangaroo.searchPage.table, function (i, row) {

            if (row[1] == document.Kangaroo.departments[department])
                newTable.push(row);
        });

        setupTable({aaData: newTable});
    }
}


