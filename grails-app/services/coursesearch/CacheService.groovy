package coursesearch

/**
 * Stores information in the super-fast, memory-only redis cache. This is a great way to speed up slow, unchanging queries.
 */
class CacheService {

    def redisService

    def dataTablesService

    static transactional = true

    def initializeCache() {
        clearCache();

        println "Pre-caching data..."
        dataTablesService.getTableCached()

        Professor.list().each { prof -> prof.getColleagues() }
    }

    def clearCache() {
        println "Clearing redis cache..."
        redisService.withRedis { redis -> redis.flushDB() }
    }
}
