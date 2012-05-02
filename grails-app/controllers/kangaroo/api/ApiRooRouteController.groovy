package kangaroo.api

import grails.converters.JSON

class ApiRooRouteController {

    def rooRouteDataService

    def index = {

        render(rooRouteDataService.stopList as JSON);
    }
}
