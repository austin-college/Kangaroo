modules = {
    common {
        dependsOn "scripts, bootstrap"

        resource url: "css/app2.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_common'
        resource url: "css/profiles.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_common'
        resource url: "css/professorView.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_common'
        resource url: "css/admin.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_common'
        resource url: "css/courseView.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_common'
        resource url: "css/searchPage2.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_common'
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

        resource url:"libraries/fullcalendar/fullcalendar.js", bundle: "bundle_professor"
        resource url:"libraries/fullcalendar/jquery-ui-1.8.11.custom.min.js", bundle: "bundle_professor"
        resource url:"libraries/fullcalendar/fullcalendar.css", bundle: "bundle_professor"
        resource url:"js/professorCalendar.js", bundle: "bundle_professor"
    }

    error {
        resource url: "css/errorPage.less", attrs: [rel: "stylesheet/less", type: 'css'], bundle: 'bundle_error'
        resource url: "js/errorReporting.js", bundle: 'bundle_error'
    }

    printView {
        dependsOn "common"
        resource url: "css/printCalendar.less", attrs: [rel: "stylesheet/less", type: 'css', media: 'screen,print'], bundle: 'bundle_print'
    }

    bootstrap {
        resource url: "libraries/bootstrap.137.min.css"
    }

    scripts {
        dependsOn "jquery"
    }
}