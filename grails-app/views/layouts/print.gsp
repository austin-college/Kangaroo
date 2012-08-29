%{--------------------------------------------------------------------

    print.gsp

    The printable template, used for printing calendars.

---------------------------------------------------------------------}%

<%@ page import="grails.util.Environment; kangaroo.Term" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript">var _sf_startpt = (new Date()).getTime()</script>

    <!-- Random number: ${new Random().nextInt(100)} -->
    <title><g:layoutTitle default="Kangaroo"/></title>
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>

    <r:require modules="printView"/>

    %{-- Set some global javascript variables while we still have access to the grails closures. --}%
    <script type="text/javascript">
        var contextPath = "${request.contextPath}";
    </script>

    <r:layoutResources/>
    <g:layoutHead/>

    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <g:if test="${Environment.current == Environment.PRODUCTION}">
        <g:render template="/global/headerScripts"/>
    </g:if>
</head>

<body>

<g:layoutBody/>
<r:layoutResources/>

%{-- Load analytics in production. --}%
<g:if test="${Environment.current == Environment.PRODUCTION}">
    <g:render template="/global/footerScripts"/>
</g:if>
</body>
</html>