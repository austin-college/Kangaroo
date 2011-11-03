package coursesearch

import grails.util.Environment

/**
 * Stores information in the super-fast, memory-only redis cache. This is a great way to speed up slow, unchanging queries.
 */
class CacheService {

    def redisService

    def dataTablesService

    static transactional = true

    def initializeCache() {
        clearCache();

        Term.list().each { term ->
            println "Pre-caching table for ${term.fullDescription}..."
            dataTablesService.getTableCached(term)
        }

        // Pre-cache the colleagues list, an expensive operation.
        if (Environment.current == Environment.PRODUCTION) {
            print "Pre-caching colleagues..."
            def i = 0;
            Professor.list().each {
                prof ->
                prof.getColleagues();
                if (i++ % 4 == 0)
                    print ".${i}.."
            }
        }
        println "CACHED!"
    }

    def clearCache() {
        println "Clearing redis cache..."
        redisService.withRedis { redis -> redis.flushDB() }
    }
}
