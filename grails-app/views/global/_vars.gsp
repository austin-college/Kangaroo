<%@ page import="kangaroo.Department; kangaroo.Term; kangaroo.Setting" %>

%{-- Write some variables into the page so that JavaScript has access to them immediately. --}%
<script type="text/javascript">
    document.Kangaroo = {
        "contextPath":"${request.contextPath}",
        "currentTerm":"${Setting.getSetting("currentTermCode").encodeAsJavaScript()}",
        "defaultSearchTerm":"${Setting.getSetting("defaultSearchTermCode").encodeAsJavaScript()}",
        "departments":{
            ${Department.list().collect { department -> "\"${department.id}\": \"${department.name}\""}.join(", ") }
        },
        "terms":{
            ${kangaroo.Term.list().collect { term -> "\"${term.id}\": \"${term.fullDescription}\"" }.join(", ") }
        },
        "url":function (path) {
            return document.Kangaroo.contextPath + path;
        }
    };

</script>