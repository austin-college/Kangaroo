package kangaroo.api

import grails.converters.JSON
import kangaroo.Building

class ApiBuildingController {

    def index = { render(Building.list().collectEntries { [it.key, it]} as JSON) }

}
