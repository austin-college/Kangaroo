package kangaroo

class JQueryUITagLib {

    static namespace = "jq"

    /**
     * @param type : can be "warning", "error", "success", "info". Defaults to "success".
     */
    def accordion = { attrs, body ->
        out << "<h3><a href=\"#\">${attrs.title}</a></h3>\n";
        out << "<div>" << body() << "</div>"
        //<div class="alert-message block-message ${type}"><a class="close" href="#">×</a><p>${attrs.message}</p></div>""";
    }

}
