package kangaroo.admin

import grails.converters.JSON
import kangaroo.mn.ProfessorOfficeHours
import kangaroo.*

class BatchControlController {

    def backendDataService
    def courseImporterService
    def facultyDataService
    def textbookDataService
    def amazonDataService
    def majorDataService
    def cacheService

    // Define all the batch jobs here.
    def jobs = [

            "courses": [
                    id: "courses",
                    name: "Courses",
                    run: {
                        Term.list().each { term ->
                            courseImporterService.importFromJson(term)
                        }
                    },
                    status: {"${Course.count()} imported"}
            ],
            "faculty": [
                    id: "faculty",
                    name: "Faculty",
                    run: { facultyDataService.fetchAndMatch() },
                    status: {"${Professor.count()} imported; ${Professor.countByMatched(true)} matched (${AppUtils.toPercent(Professor.countByMatched(true) / Professor.count())}%)"}
            ],
            "textbooks": [
                    id: "textbooks",
                    name: "Textbooks",
                    run: { textbookDataService.lookupTextbooksForAllCourses() },
                    status: {
                        if (Course.count() > 0)
                            "${Textbook.count()} textbooks;"
                        else
                            "No courses so no textbooks"
                    }
            ],
            "amazon": [
                    id: "amazon",
                    name: "Amazon",
                    run: { amazonDataService.lookupAllTextbooks() },
                    status: {
                        if (Textbook.count() > 0)
                            "${Textbook.countByMatchedOnAmazon(true)} textbooks have Amazon details (${AppUtils.toPercent(Textbook.countByMatchedOnAmazon(true) / Textbook.count())}%)"
                        else
                            "${Textbook.countByMatchedOnAmazon(true)} textbooks have Amazon details (0%)"
                    }
            ],
            "clearCache": [
                    id: "clearCache",
                    name: "Clear Cache",
                    run: { cacheService.clearCache() },
                    status: { "Ready" }
            ],
            "initializeCache": [
                    id: "initializeCache",
                    name: "Initialize Cache",
                    run: { cacheService.initializeCache() },
                    status: { "Ready" }
            ],
            "cacheColleagues": [
                    id: "cacheColleagues",
                    name: "Cache Colleagues",
                    run: { cacheService.cacheColleagues() },
                    status: { "Ready" }
            ],
            "clearOfficeHours": [
                    id: "clearOfficeHours",
                    name: "clearOfficeHours",
                    run: { ProfessorOfficeHours.list().each { it.delete(flush: true) } },
                    status: { "Ready" }
            ]
    ]

    def index = {}

    def getJobList = {
        def data = [:]
        jobs.each { job -> data[job.key] = jobToJson(job.value) }
        render(data as JSON)
    }

    def runJob = {
        def job = jobs[params.job];
        if (job) {
            def results
            def time = AppUtils.time { results = job.run(); }
            render([success: true, time: time, details: jobToJson(job)] as JSON)
        }
        else
            render([error: "InvalidJob"] as JSON)
    }

    def jobToJson(job) {
        [id: job.id, name: job.name, status: job.status(), html: g.render(template: "job", model: [job: job])]
    }
}
