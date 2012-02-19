<%@ page import="grails.util.Environment; kangaroo.Term" %>
<!DOCTYPE html>
<html xmlns:fb="http://www.facebook.com/2008/fbml" xmlns:og="http://opengraphprotocol.org/schema/">
<head>
    <!-- Random number: ${new Random().nextInt(100)} -->
    <script type="text/javascript">var _sf_startpt = (new Date()).getTime()</script>
    <title><g:layoutTitle default="Kangaroo"/></title>
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
    <link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.3.0/bootstrap.min.css">
    <link rel="stylesheet" media="screen, print" title="Printer-Friendly Style"
          href="${resource(dir: 'css', file: 'printCalendar.css')}"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>

    %{-- Set some global javascript variables while we still have access to the grails closures. --}%
    <script type="text/javascript">
        var contextPath = "${request.contextPath}";
    </script>

    <g:layoutHead/>

<!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <g:if test="${Environment.current == Environment.PRODUCTION}">
        <!-- start Mixpanel --><script type="text/javascript">var mpq = [];
    mpq.push(["init", "102753bbc8bcef0e34932d5f829ed00d"]);
    (function () {
        var b, a, e, d, c;
        b = document.createElement("script");
        b.type = "text/javascript";
        b.async = true;
        b.src = (document.location.protocol === "https:" ? "https:" : "http:") + "//api.mixpanel.com/site_media/js/api/mixpanel.js";
        a = document.getElementsByTagName("script")[0];
        a.parentNode.insertBefore(b, a);
        e = function (f) {
            return function () {
                mpq.push([f].concat(Array.prototype.slice.call(arguments, 0)))
            }
        };
        d = ["init", "track", "track_links", "track_forms", "register", "register_once", "identify", "name_tag", "set_config"];
        for (c = 0; c < d.length; c++) {
            mpq[d[c]] = e(d[c])
        }
    })();
    </script><!-- end Mixpanel -->
    </g:if>
</head>

<body>

<g:layoutBody/>
</body>
</html>