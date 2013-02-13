package kangaroo.api

import grails.converters.JSON
import kangaroo.PhoneNumber

class ApiV1Controller {

    // api/importantNumbers
    def importantNumbers = {
        render(PhoneNumber.list() as JSON)
    }
}
