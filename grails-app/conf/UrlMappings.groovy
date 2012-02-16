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

        // Errors...
        "500"(controller: "error", action: "serverError")
        // "404"(controller: "error", action: "notFound")
        //"403"(controller: "error", action: "forbidden")
    }
}
