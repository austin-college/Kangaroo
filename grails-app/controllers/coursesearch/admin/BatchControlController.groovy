package coursesearch.admin

import coursesearch.data.CourseDataService
import coursesearch.mn.ProfessorOfficeHours
import grails.converters.JSON
import coursesearch.Course
import coursesearch.Professor
import coursesearch.CourseUtils
import coursesearch.Textbook
import coursesearch.MeetingTime

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
                                "Michael Higgs": ["MW     01:00PM 02:00PM", "TTH    11:00AM 12:00PM"],
                                "Aaron Block": ["MWF    11:00AM 01:30PM"],
                        ]

                        profs.each { name, hours ->
                            def prof = Professor.findByName(name);

                            if (prof) {
                                println "Setting office hours for ${prof}..."
                                hours.each { schedule ->
                                    new ProfessorOfficeHours(professor: prof, meetingTime: MeetingTime.findOrCreate(CourseDataService.convertMeetingTime(schedule))).save()
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
