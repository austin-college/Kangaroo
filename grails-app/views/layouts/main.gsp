%{--------------------------------------------------------------------

    main.gsp

    The main template, used for almost all pages in Kangaroo.

---------------------------------------------------------------------}%

<%@ page import="grails.util.Environment; kangaroo.Term" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript">var _sf_startpt = (new Date()).getTime()</script>
    <g:render template="/global/commonHead"/>
    <r:require modules="common"/>

    <r:layoutResources/>
    <g:layoutHead/>

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

<r:layoutResources/>

%{-- Load analytics in production. --}%
<g:if test="${Environment.current == Environment.PRODUCTION}">
    <g:render template="/global/footerScripts"/>
</g:if>
</body>
</html>