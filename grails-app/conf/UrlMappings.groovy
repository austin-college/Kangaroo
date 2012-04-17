class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        // Assign the root.
        "/"(controller: 'home')

        // Fancy URLS for professors & courses.
        "/$id"(controller: "professor", action: "show")
        "/professor/$id"(controller: "professor", action: "show")
        "/course/$id"(controller: "course", action: "show")
        "/setOfficeHours/$id"(controller: "professor", action: "setOfficeHours")
        "/printCalendar/$id"(controller: "professor", action: "printWeeklyCalendar")

        // Manually define other controllers here (otherwise they'll be swallowed by /$id --> professor/show/id!)
        "/batchControl"(controller: "batchControl")
        "/majors"(controller: "majors")
        "/error"(controller: "error")
        "/professors"(controller: "professorSearch")
        "/data"(controller: "data")

        // Errors...
        "500"(controller: "error", action: "serverError")
        "/robots.txt"(controller: "home", action: "robots")
        // "404"(controller: "error", action: "notFound")
        //"403"(controller: "error", action: "forbidden")
    }
}
