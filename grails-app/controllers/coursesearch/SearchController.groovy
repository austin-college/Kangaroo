package coursesearch

import grails.converters.JSON
import redis.clients.jedis.Jedis

class SearchController {

    def searchService
    def redisService

    def index = { }

    def getClassesForTable = {

        def start = System.currentTimeMillis()

        String results = redisService.memoize("courses") { Jedis redis -> searchService.coursesTableJSON };
        render(contentType: "application/json", text: results);
        println "done in ${(System.currentTimeMillis() - start) / 1000.0} seconds"
    }
}
