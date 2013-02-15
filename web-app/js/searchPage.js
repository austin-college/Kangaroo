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

            // Expand the received data into true JSON objects.
            var courses = $.map(response.courses, function (array) {
                return  {
                    id: array[0],
                    name: array[1],
                    department: array[2],
                    professors: $.map(array[3], function (array) {
                        return { id: array[0], name: array[1] };
                    }),
                    meetingTimes: $.map(array[4], function (array) {
                        return { id: array[0], name: array[1] };
                    })
                };
            });

            var tableRows = $.map(courses, function (course) {
                return [tableRowFromCourse(course)];
            });

            document.Kangaroo.searchPage.data = courses;
            document.Kangaroo.searchPage.table = {
                "aaData": tableRows,
                "iTotalRecords": tableRows.length,
                "iTotalDisplayRecords": tableRows.length
            };
            setupTable(document.Kangaroo.searchPage.table);
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
    }).join("<br >/>");
    var meetingTimeLinks = $.map(course.meetingTimes,function (obj) {
        return "<a href='" + document.Kangaroo.url("/course/bySchedule/" + obj.id) + "'>" + obj.name + "</a>";
    }).join("<br />");

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


