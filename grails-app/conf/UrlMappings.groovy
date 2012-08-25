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
        "/login"(controller: "login", action: "auth")
        "/logout"(controller: "logout")

        // API
        "/api/"(controller: "apiHome")
        "/api/person/"(controller: "apiPerson", action: "list")
        "/api/person/$id?"(controller: "apiPerson", parseRequest: true) {
            action = [GET: "show", PUT: "update", DELETE: "delete", POST: "save"]
        }
        "/api/person/$id/status"(controller: "apiPerson", action: "status", parseRequest: true)
        "/api/term/"(controller: "apiTerm", action: "list")
        "/api/term/$id?"(controller: "apiTerm", parseRequest: true) {
            action = [GET: "show", PUT: "update", DELETE: "delete", POST: "save"]
        }
        "/api/course/"(controller: "apiCourse", action: "list")
        "/api/course/$id?"(controller: "apiCourse", parseRequest: true) {
            action = [GET: "show", PUT: "update", DELETE: "delete", POST: "save"]
        }
        "/api/rooRoute"(controller: "apiRooRoute", action: "index")

        // Errors...
        "500"(controller: "error", action: "serverError")
        "/robots.txt"(controller: "home", action: "robots")
        // "404"(controller: "error", action: "notFound")
        //"403"(controller: "error", action: "forbidden")
    }
}
