import coursesearch.Course
import grails.converters.JSON
import redis.clients.jedis.Jedis

class BootStrap {

    def courseListParseService
    def redisService

    def init = { servletContext ->
        // Customize how objects are formatted to JSON.
        JSON.registerObjectMarshaller(Course) {
            return [id: it.id, name: it.name, items: it.items];
        }

        if (Course.count() == 0)
            courseListParseService.parseScrape();
        redisService.withRedis { Jedis redis -> redis.del("courses")}
    }

    def destroy = {
    }
}
