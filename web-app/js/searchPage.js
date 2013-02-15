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
    courses: {},
    rows: {}
};

$(document).ready(function () {
    // Store the empty table HTML so we can recreate it later.
    document.Kangaroo.searchPage.emptyTableHtml = $("#tableHolder").html();

    $("#terms").change(refetchTable);
    $("#departments").change(refilterTable);
    $("#requirements").change(refilterTable);

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

            // Expand the received data into true JSON objects.
            var courses = $.map(response.courses, function (array) {
                return  {
                    id: array[0],
                    name: array[1],
                    department: { id: array[2][0], name: array[2][1] },
                    professors: $.map(array[3], function (array) {
                        return { id: array[0], name: array[1] };
                    }),
                    meetingTimes: $.map(array[4], function (array) {
                        return { id: array[0], name: array[1] };
                    }),
                    requirementsFulfilled: array[5]
                };
            });
            document.Kangaroo.searchPage.courses = courses;

            // Now build the table!
            refilterTable();
        }
    });
}

/**
 * Given a course object, turns it into a table row.
 */
function tableRowFromCourse(course) {
    var courseName = "<a href='" + document.Kangaroo.url("/course/" + course.id) + "'>" + course.name + "</a>";
    var professorLinks = $.map(course.professors,function (obj) {
        return "<a href='" + document.Kangaroo.url("/" + obj.id) + "'>" + obj.name + "</a>";
    }).join("<br />");
    var meetingTimeLinks = $.map(course.meetingTimes,function (obj) {
        return "<a href='" + document.Kangaroo.url("/course/bySchedule/" + obj.id) + "'>" + obj.name + "</a>";
    }).join("<br />");

    if (!course.professors.length) {
        professorLinks = "<i>Unknown</i>";
    }
    if (!course.meetingTimes.length) {
        meetingTimeLinks = "<i>Unknown</i>";
    }

    return [courseName, course.department.name, professorLinks, meetingTimeLinks];
}

/**
 * Re-fetches the table using the current on-page settings.
 */
function refetchTable() {
    getTableData($("#terms").val());
}

/**
 * Re-filters and builds the table using the current on-page settings.
 */
function refilterTable() {
    var departmentID = $("#departments").val();
    var requirementID = $("#requirements").val();

    var tableRows = $.map(document.Kangaroo.searchPage.courses, function (course) {
        if ((departmentID != course.department.id) && (departmentID != "ANY"))
            return null;
        if ((requirementID != "ANY") && $.inArray(requirementID, course.requirementsFulfilled) == -1)
            return null;

        return [tableRowFromCourse(course)];
    });
    var table = {
        "aaData": tableRows,
        "iTotalRecords": tableRows.length,
        "iTotalDisplayRecords": tableRows.length
    };
    setupTable(table);
}

