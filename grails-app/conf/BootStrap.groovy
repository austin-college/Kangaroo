import coursesearch.Course
import coursesearch.Department
import grails.converters.JSON
import redis.clients.jedis.Jedis

class BootStrap {

    def courseListParseService
    def facultyFetcherService
    def redisService

    def init = { servletContext ->
        // Customize how objects are formatted to JSON.
        JSON.registerObjectMarshaller(Course) {
            return [id: it.id, name: it.name, items: it.items];
        }

        if (Course.count() == 0)
            courseListParseService.parseScrape();

        def departmentMapping = [
                "ANTH": "Anthropology",
                "ART": "Art",
                "ARTH": "Art History",
                "ASST": "Asian Studies",
                "BA": "Business Administration",
                "BIOL": "Biology",
                "CHEM": "Chemistry",
                "CHIN": "Chinese",
                "CI": "C/I",
                "CL": "Communication/Leadership",
                "CLAS": "Classics",
                "COMM": "Communications",
                "CS": "Computer Science",
                "ECO": "Economics",
                "EDUC": "Education",
                "ENG": "English",
                "ENVS": "Environmental Studies",
                "ESS": "Exercise/Sports Sciences",
                "FR": "French",
                "GER": "German",
                "GNDR": "Gender Studies",
                "GRK": "Greek",
                "GS": "General Studies",
                "HIST": "History",
                "JAPN": "Japanese",
                "LAT": "Latin",
                "LEAD": "Leadership",
                "LS": "Life Sports",
                "MATH": "Mathematics",
                "MEDA": "Media",
                "ML": "Modern Language",
                "MUS": "Music",
                "PHIL": "Philosophy",
                "PHY": "Physics",
                "PSCI": "Political Science",
                "PSY": "Psychology",
                "REL": "Religious Studies",
                "SOC": "Sociology",
                "SPAN": "Spanish",
                "THEA": "Theater"
        ]

        departmentMapping.each { mapping ->
            def dept = Department.findByCode(mapping.key);
            if (dept) {
                if (dept.name != mapping.value) {
                    dept.name = mapping.value
                    dept.save();
                }
            }
            else
                new Department(code: mapping.key, name: mapping.value).save();
        }

        facultyFetcherService.downloadFaculty()
        redisService.withRedis { Jedis redis -> redis.del("courses")}
    }

    def destroy = {
    }
}
