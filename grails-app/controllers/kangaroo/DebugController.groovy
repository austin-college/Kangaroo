package kangaroo

import grails.util.Environment

class DebugController {

    def cacheService

    def beforeInterceptor = {
        return (Environment.current == Environment.DEVELOPMENT);
    }

    def clearCache = {
        cacheService.clearCache()
        redirect(controller: 'home')
    }
}
