class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }
        "/majors"(view: '/majors')
        "/"(controller: 'home')
        "500"(view: '/error')
    }
}
