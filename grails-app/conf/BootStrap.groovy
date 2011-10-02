import coursesearch.Course
import grails.converters.JSON
import redis.clients.jedis.Jedis

class BootStrap {

    // Services to initialize our data.
    def departmentDataService
    def courseDataService
    def facultyDataService

    def redisService

    def init = { servletContext ->

        // Customize how objects are formatted to JSON.
        JSON.registerObjectMarshaller(Course) {
            return [id: it.id, name: it.name, items: it.items];
        }

        departmentDataService.setUpDepartments()
        if (Course.count() == 0)
            courseDataService.downloadAndProcess();


        facultyDataService.fetchAndMatch()
        redisService.withRedis { Jedis redis -> redis.del("courses")}
    }

    def destroy = {
    }
}
