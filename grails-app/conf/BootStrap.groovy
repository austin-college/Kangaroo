import coursesearch.Course
import grails.converters.JSON
import redis.clients.jedis.Jedis

class BootStrap {

    def departmentDataService
    def courseListParseService
    def facultyFetcherService
    def redisService

    def init = { servletContext ->

        // Customize how objects are formatted to JSON.
        JSON.registerObjectMarshaller(Course) {
            return [id: it.id, name: it.name, items: it.items];
        }

        departmentDataService.setUpDepartments()
        if (Course.count() == 0)
            courseListParseService.parseScrape();


        facultyFetcherService.fetchAndMatch()
        redisService.withRedis { Jedis redis -> redis.del("courses")}
    }

    def destroy = {
    }
}
