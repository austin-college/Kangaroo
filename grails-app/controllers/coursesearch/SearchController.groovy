package coursesearch

import grails.converters.JSON
import redis.clients.jedis.Jedis

class SearchController {

    def searchService
    def redisService

    def index = { }

    def getClassesForTable = {

        def start = System.currentTimeMillis()

        def rendered = redisService.memoize("courses") { Jedis redis ->
            println "Re-generating...."
            def ret = (searchService.getCoursesForTable(params) as JSON)
            return ret;
        }

        render(rendered);
        println "done in ${(System.currentTimeMillis() - start) / 1000.0} seconds"
    }
}
