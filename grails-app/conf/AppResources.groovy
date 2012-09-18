modules = {
    common {
        dependsOn "scripts, bootstrap"

        resource url: "less/app.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_common'
        resource url: "less/profiles.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_common'
        resource url: "less/professorView.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_common'
        resource url: "less/admin.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_common'
        resource url: "less/courseView.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_common'
        resource url: "less/searchPage.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_common'
        resource url: "js/cookies.js", bundle: 'bundle_common'
    }

    search {
        dependsOn "common"

        resource url: "js/jquery.dataTables.min.js", bundle: 'bundle_search'
        resource url: "libraries/contextMenu/jquery.contextMenu.js", bundle: 'bundle_search'
        resource url: "js/courseDataTable.js", bundle: 'bundle_search'
        resource url: "js/searchPage.js", bundle: 'bundle_search'
        resource url: "libraries/contextMenu/jquery.contextMenu.css", bundle: 'bundle_search'
    }

    professor {
        dependsOn "common"

        resource url: "libraries/fullcalendar/fullcalendar.js", bundle: "bundle_professor"
        resource url: "libraries/fullcalendar/jquery-ui-1.8.11.custom.min.js", bundle: "bundle_professor"
        resource url: "libraries/fullcalendar/fullcalendar.css", bundle: "bundle_professor"
        resource url: "js/professorCalendar.js", bundle: "bundle_professor"
    }

    login {
        dependsOn "common"
        resource url: "less/login.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_login'
    }

    error {
        resource url: "less/errorPage.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_error'
        resource url: "js/errorReporting.js", bundle: 'bundle_error'
    }

    printView {
        dependsOn "scripts, bootstrap"
        resource url: "libraries/fullcalendar/fullcalendar.js", attrs: [media: 'screen,print'], bundle: "bundle_print"
        resource url: "libraries/fullcalendar/jquery-ui-1.8.11.custom.min.js", attrs: [media: 'screen,print'], bundle: "bundle_print"
        resource url: "libraries/fullcalendar/fullcalendar.css", attrs: [media: 'screen,print'], bundle: "bundle_print"
        resource url: "less/printCalendar.less", attrs: [rel: "stylesheet/less", type: 'css', media: 'screen,print'], bundle: 'bundle_print'
    }

    bootstrap {
        resource url: "libraries/bootstrap.137.min.css", attrs: [media: 'screen,print']
    }

    scripts {
        dependsOn "jquery"
    }
}