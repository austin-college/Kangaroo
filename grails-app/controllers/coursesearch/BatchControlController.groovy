package coursesearch

import grails.converters.JSON

class BatchControlController {

    def courseDataService
    def facultyDataService

    static def jobs = ["courses", "faculty"]//, "textbooks", "amazon"]

    def index = {}

    def getJobs = {
        def data = [:]
        jobs.each { job -> data[job] = getDetailsAndHtml(job) }
        render(data as JSON)
    }

    def runJob = {
        def results
        def time = CourseUtils.time {
            switch (params.job) {

                case "courses":
                    results = courseDataService.downloadAndProcess()
                    break

                case "faculty":
                    results = facultyDataService.fetchAndMatch()
                    break

                default:
                    render([error: "InvalidJob"] as JSON)
                    return;
            }
        }

        render([success: true, time: time, details: getDetailsAndHtml(params.job)] as JSON)
    }

    def getDetails = {
        render(getDetailsAndHtml(params.job) as JSON)
    }

    def getDetails(job) {
        switch (job) {
            case "courses":
                return [name: "Courses", status: "${Course.count()} imported"];

            case "faculty":
                return [name: "Faculty", status: "${Professor.count()} imported; ${Professor.countByMatched(true)} matched (${toPercent(Professor.countByMatched(true) / Professor.count())}%)"]
        }
    }

    def getDetailsAndHtml(job) {
        def details = getDetails(job)
        details.id = job;
        details.html = g.render(template: "job", model: [job: details]);
        details;
    }

    def getTextbookDetails() {
        def percentCourses = Course.count() == 0 ? 0 : toPercent(Course.countByTextbooksParsed(true) / Course.count());
        def percentAmazonDetails = Textbook.count() == 0 ? 0 : toPercent(Textbook.countByMatchedOnAmazon(true) / Textbook.count());
        [numTextbooks: Textbook.count(), numLookedUp: Textbook.countByMatchedOnAmazon(true),
                percentCoursesWithBooks: percentCourses, percentLookedUp: percentAmazonDetails]


    }

    int toPercent(value) {
        (int) ((100.0 * (double) value).round())
    }
}
