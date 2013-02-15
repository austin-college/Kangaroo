<%@ page import="grails.util.Environment" %>
<div id="userLinks">
    <sec:ifLoggedIn>
        <g:link controller="logout">Logout</g:link>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
        <g:link controller="login">Login</g:link>
    </sec:ifNotLoggedIn>

    <g:if test="${Environment.current == Environment.DEVELOPMENT}">
        &middot; <g:link controller="debug" action="clearCache">Clear Cache</g:link>
    </g:if>
</div>