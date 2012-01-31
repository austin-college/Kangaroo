class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }
        "/professor/$id"(controller: "professor", action: "show")
        "/course/$id"(controller: "course", action: "show")
        "/"(controller: 'home')
        "500"(view: '/error')
    }
}
