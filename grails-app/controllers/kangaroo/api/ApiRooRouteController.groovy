package kangaroo.api

import grails.converters.JSON
import kangaroo.RooRouteStop

class ApiRooRouteController {

    def index = { render(RooRouteStop.list().collectEntries { [it.outbackId, it]} as JSON) }
}
