%{--------------------------------------------------------------------

    print.gsp

    The printable template, used for printing calendars.

---------------------------------------------------------------------}%

<%@ page import="grails.util.Environment; kangaroo.Term" %>
<!DOCTYPE html>
<html xmlns:fb="http://www.facebook.com/2008/fbml" xmlns:og="http://opengraphprotocol.org/schema/">
<head>
    <!-- Random number: ${new Random().nextInt(100)} -->
    <title><g:layoutTitle default="Kangaroo"/></title>
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
    <link rel="stylesheet" href="${resource(dir: 'libraries', file: 'bootstrap.137.min.css')}"/>
    <script type="text/javascript" src="${resource(dir: 'libraries', file: 'jquery.164.min.js')}"></script>

    <less:stylesheet media="screen, print" name="printCalendar"/>
    <less:scripts/>

    %{-- Set some global javascript variables while we still have access to the grails closures. --}%
    <script type="text/javascript">
        var contextPath = "${request.contextPath}";
    </script>

    <g:layoutHead/>

    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
</head>

<body>

<g:layoutBody/>

<g:if test="${Environment.current == Environment.PRODUCTION}">
    <g:render template="/global/analytics"/>
</g:if>
</body>
</html>