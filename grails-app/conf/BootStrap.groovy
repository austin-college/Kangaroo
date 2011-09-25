import grails.converters.JSON
import coursesearch.Course

class BootStrap {

    def init = { servletContext ->
         // Customize how objects are formatted to JSON.
        JSON.registerObjectMarshaller(Course) {
            return [id: it.id, name: it.name, items: it.items];
        }
    }

    def destroy = {
    }
}
