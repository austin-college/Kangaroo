package kangaroo.api

import grails.converters.JSON
import kangaroo.Building

class ApiBuildingController {

    def index = { render(Building.list() as JSON) }

}
