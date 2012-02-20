%{--------------------------------------------------------------------

    main.gsp

    The main template, used for almost all pages in Kangaroo.

---------------------------------------------------------------------}%

<%@ page import="grails.util.Environment; kangaroo.Term" %>
<!DOCTYPE html>
<html xmlns:fb="http://www.facebook.com/2008/fbml" xmlns:og="http://opengraphprotocol.org/schema/">
<head>
    <g:render template="/global/commonHead"/>
    <g:layoutHead/>
    <less:scripts/>
</head>

<body>
<div class="container">

    <div class="content">
        <g:render template="/global/logo"/>

        %{-- Render the body inside the wrapper. --}%
        <div class="row">
            <div class="span16">
                <g:layoutBody/>
            </div>
        </div>

        %{-- Render the "like" button at the bottom. --}%
        <g:if test="${Environment.current == Environment.PRODUCTION}">
            <g:render template="/global/likeButton"/>
        </g:if>
    </div>

    <g:render template="/global/footer"/>
</div>
</body>
</html>