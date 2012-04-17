<%@ page import="grails.util.Environment" %>
%{-- Render the common <head> elements. --}%

<!-- Random number (caching test): ${new Random().nextInt(100)} -->
<title><g:layoutTitle default="Kangaroo"/></title>
<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>

%{-- Set some global javascript variables while we still have access to the grails closures. --}%
<script type="text/javascript">
    var contextPath = "${request.contextPath}";
</script>

%{-- Set Facebook details (affects the Like button) --}%
<g:render template="/global/facebookOpenGraph"/>

%{-- Set canonical URLs to prevent duplicate content. --}%
<link rel="canonical" href="http://kangaroo.austincollege.edu${request.forwardURI - request.contextPath}"/>

<!-- Load HTML5 shim, for IE6-8 support of HTML elements -->
<!--[if lt IE 9]>
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

<g:javascript src="cookies.js"/>

%{-- Load analytics in production. --}%
<g:if test="${Environment.current == Environment.PRODUCTION}">
    <g:render template="/global/headerScripts"/>
</g:if>