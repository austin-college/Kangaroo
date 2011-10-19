package coursesearch

import grails.converters.JSON

class BatchControlController {

    def courseDataService
    def facultyDataService
    def textbookDataService
    def amazonDataService

    // Define all the batch jobs here.
    def jobs = [

            "courses": [
                    id: "courses",
                    name: "Courses",
                    run: { courseDataService.downloadAndProcess() },
                    status: {"${Course.count()} imported"}
            ],
            "faculty": [
                    id: "faculty",
                    name: "Faculty",
                    run: { facultyDataService.fetchAndMatch() },
                    status: {"${Professor.count()} imported; ${Professor.countByMatched(true)} matched (${toPercent(Professor.countByMatched(true) / Professor.count())}%)"}
            ],
            "textbooks": [
                    id: "textbooks",
                    name: "Textbooks",
                    run: { textbookDataService.lookupTextbooksForAllCourses() },
                    status: {"${Textbook.count()} textbooks; ${toPercent(Course.countByTextbooksParsed(true) / Course.count())}% of courses have books"}
            ],
            "amazon": [
                    id: "amazon",
                    name: "Amazon",
                    run: { amazonDataService.lookupAllTextbooks() },
                    status: {
                        if ( Textbook.count() > 0)
                            "${Textbook.countByMatchedOnAmazon(true)} textbooks have Amazon details (${toPercent(Textbook.countByMatchedOnAmazon(true) / Textbook.count())}%)"
                        else
                            "${Textbook.countByMatchedOnAmazon(true)} textbooks have Amazon details (0%)"
                    }
            ],
    ]

    def index = {}

    def getJobs = {
        def data = [:]
        jobs.each { job -> data[job.key] = jobToJson(job.value) }
        render(data as JSON)
    }

    def runJob = {
        def job = jobs[params.job];
        println job
        if (job) {
            def results
            def time = CourseUtils.time { results = job.run(); }
            render([success: true, time: time, details: jobToJson(job)] as JSON)
        }
        else
            render([error: "InvalidJob"] as JSON)
    }

    def jobToJson(job) {
        [id: job.id, name: job.name, status: job.status(), html: g.render(template: "job", model: [job: job])]
    }

    static int toPercent(value) {
        (int) ((100.0 * (double) value).round())
    }
}
