package coursesearch

/**
 * Generates elements supported by Twitter's Bootstrap library.
 */
class BootstrapTagLib {

    static namespace = "bootstrap"

    /**
     * @param type : can be "warning", "error", "success", "info". Defaults to "success".
     */
    def blockMessage = { attrs ->
        def type = attrs?.type ?: "success"
        out << """<div class="alert-message block-message ${type}"><a class="close" href="#">×</a><p>${attrs.message}</p></div>""";
    }

    def flashMessages = {
        if (flash.error)
            out << bootstrap.blockMessage(message: flash.error, type: "error");
        if (flash.success)
            out << bootstrap.blockMessage(message: flash.success, type: "success");
        if (flash.warning)
            out << bootstrap.blockMessage(message: flash.warning, type: "warning");
        if (flash.message)
            out << bootstrap.blockMessage(message: flash.message, type: "info");
    }

    def domainErrors = { attrs ->
        if (attrs?.bean && attrs?.bean?.hasErrors()) {
            out << """<div class="alert-message block-message error"><a class="close" href="#">×</a>""";
            out << g.renderErrors(bean: attrs.bean);
            out << "</div>"
        }
    }

}