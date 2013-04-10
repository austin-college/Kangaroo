package kangaroo.admin

import grails.converters.JSON
import kangaroo.AppUtils
import kangaroo.Course
import kangaroo.Term
import kangaroo.Textbook
import kangaroo.mn.ProfessorOfficeHours

class AdminJobController {

    def backendDataService
    def courseImporterService
    def facultyDataService
    def textbookFetchService
    def amazonFetchService
    def majorDataService
    def cacheService

    // Define all the batch jobs here.
    def jobs = [

            "13SP": [
                    id: "13SP",
                    name: "Import 13SP",
                    run: { courseImporterService.importCourses(Term.get("13SP")) },
                    status: { "Ready; ${Course.countByTerm(Term.get("13SP"))} existing courses to scrub" }
            ],
            "13FA": [
                    id: "13FA",
                    name: "Import 13FA",
                    run: { courseImporterService.importFromJson(Term.findOrCreate("13FA"), new URL("http://pastebin.com/raw.php?i=T5aeGT2a").text) },
                    status: { "Ready; ${Course.countByTerm(Term.findOrCreate("13FA"))} existing courses to scrub" }
            ],
            "reImport": [
                    id: "reImport",
                    name: "Re-Import Data",
                    run: { backendDataService.reset() },
                    status: { "Ready" }
            ],
            "courses": [
                    id: "courses",
                    name: "Courses",
                    run: {
                        Term.list().each { term ->
                            courseImporterService.importFromJson(term)
                        }
                    },
                    status: { "${Course.count()} imported" }
            ],
            "textbooks": [
                    id: "textbooks",
                    name: "Textbooks",
                    run: { textbookFetchService.lookupTextbooksForAllCourses() },
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
                    run: { amazonFetchService.lookupAllTextbooks() },
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
                    name: "Drop & Re-Initialize Cache",
                    run: { cacheService.clearCache(); cacheService.initializeCacheIfNeeded() },
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
        } else
            render([error: "InvalidJob"] as JSON)
    }

    def jobToJson(job) {
        [id: job.id, name: job.name, status: job.status(), html: g.render(template: "job", model: [job: job])]
    }
}
