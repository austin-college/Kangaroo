package kangaroo.api

import kangaroo.PhoneNumber
import grails.converters.JSON

class ApiV1Controller {

    // api/importantNumbers
    def importantNumbers = {
        render(PhoneNumber.list() as JSON)
    }
}
