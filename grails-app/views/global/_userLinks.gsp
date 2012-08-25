<div id="userLinks">
    <sec:ifLoggedIn>
        <g:link controller="logout">Logout</g:link>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
        <g:link controller="login">Login</g:link>
    </sec:ifNotLoggedIn>
</div>