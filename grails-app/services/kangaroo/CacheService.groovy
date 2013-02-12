package kangaroo

/**
 * Caches information in-memory.
 *
 * Note: Editing this file while in development mode erases the cache.
 */
class CacheService {

    def dataTablesService

    static transactional = false

    private static cache = [:]

    def memoize(key, Closure closure) {
        if (cache[key])
            return cache[key]
        else {
            println "Cache miss for ${key}"
            def value = closure();
            cache[key] = value;
            return value;
        }
    }

    def initializeCacheIfNeeded() {
        if (isEmpty())
            initializeCache()
        else
            println "Cache already exists; no need to initialize."
    }

    private def initializeCache() {
        println "Memoizing courses..."
        Course.list().each { course ->
            memoize("course/${course.id}/asRow") { dataTablesService.toRow(course) }
        }

        println "Cache warmed up!"
        println cache.keySet()
    }

    boolean isEmpty() {
        return cache.isEmpty()
    }

    def clearCache() {
        println "Clearing cache..."
        cache = [:]
    }
}
