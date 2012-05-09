package kangaroo

/**
 * Stores information in the super-fast, memory-only redis cache. This is a great way to speed up slow, unchanging queries.
 */
class CacheService {

    def dataTablesService

    static transactional = false

    // Cache table JSON.
    static cache = [:]

    def memoize(key, Closure closure) {
        if (cache[key])
        return cache[key]
        else {
            def value = closure();
            cache[key] = value;
            return value;
        }
    }

    def initializeCache() {
        clearCache();

        Term.list().each { term ->
            println "Pre-caching table for ${term.fullDescription}..."
            dataTablesService.getTableCached(term)
        }

        println "CACHED!"
    }

    def initializeCacheIfNeeded() {
        if (isEmpty())
            initializeCache()
        else
            println "Cache exists; no need to initialize..."
    }

    boolean isEmpty() {
        return cache.isEmpty()
    }

    def clearCache() {
        println "Clearing cache..."
        cache = [:]
    }
}
