package coursesearch.admin

import coursesearch.data.convert.ScheduleConvertService
import coursesearch.mn.ProfessorOfficeHours
import grails.converters.JSON
import coursesearch.*

class BatchControlController {

    def courseImporterService
    def facultyDataService
    def textbookDataService
    def amazonDataService

    // Define all the batch jobs here.
    def jobs = [

            "courses": [
                    id: "courses",
                    name: "Courses",
                    run: {
                        Term.list().each { term ->
                            courseImporterService.importFromJson(term, new URL("http://phillipcohen.net/accourses/courses_${term.shortCode}.json").text)
                        }
                    },
                    status: {"${Course.count()} imported"}
            ],
            "faculty": [
                    id: "faculty",
                    name: "Faculty",
                    run: { facultyDataService.fetchAndMatch() },
                    status: {"${Professor.count()} imported; ${Professor.countByMatched(true)} matched (${CourseUtils.toPercent(Professor.countByMatched(true) / Professor.count())}%)"}
            ],
            "textbooks": [
                    id: "textbooks",
                    name: "Textbooks",
                    run: { textbookDataService.lookupTextbooksForAllCourses() },
                    status: {"${Textbook.count()} textbooks; ${CourseUtils.toPercent(Course.countByTextbooksParsed(true) / Course.count())}% of courses have books"}
            ],
            "amazon": [
                    id: "amazon",
                    name: "Amazon",
                    run: { amazonDataService.lookupAllTextbooks() },
                    status: {
                        if (Textbook.count() > 0)
                            "${Textbook.countByMatchedOnAmazon(true)} textbooks have Amazon details (${CourseUtils.toPercent(Textbook.countByMatchedOnAmazon(true) / Textbook.count())}%)"
                        else
                            "${Textbook.countByMatchedOnAmazon(true)} textbooks have Amazon details (0%)"
                    }
            ],
            "officeHours": [
                    id: "officeHours",
                    name: "Office Hours",
                    run: {

                        def profs = [
                                "Michael Higgs": ["MW 01:00PM 02:00PM", "TTH 11:00AM 12:00PM"],
                                "Aaron Block": ["MWF 11:00AM 01:30PM"],
                                "J'Lee Bumpus": ["MW 03:00PM 04:30PM"],
                                "David Baker": ["MWF 03:00PM 04:00PM"],
                                "E. Don Williams": ["MTWTH 03:15PM 04:30PM"],
                                "Keith Kisselle": ["M 01:30PM 03:30PM", "T 08:30AM 11:00AM", "W 02:00PM 05:00PM",
                                        "F 12:00PM 01:00PM"]
                        ]

                        profs.each { name, hours ->
                            def prof = Professor.findByName(name);

                            if (prof) {
                                println "Setting office hours for ${prof}..."
                                hours.each { schedule ->
                                    new ProfessorOfficeHours(professor: prof, meetingTime: ScheduleConvertService.convertMeetingTime(schedule).saveOrFind()).save()
                                    prof.save();
                                }

                                println prof.officeHours
                            }
                        }
                    },
                    status: { "Ready" }
            ]
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
}
