<html>
<head>
    <meta name='layout' content='main'/>
    <title><g:message code="springSecurity.login.title"/></title>
    <r:require module="login"/>
</head>

<body>

<div class="dialog">
    <div class="header">
        Log in with your Austin College account
    </div>

    <div class="body">
        <g:if test='${flash.message}'>
            <div class="alert-message block-message error">${flash.message}</div>
        </g:if>

        <form action='${postUrl}' method='POST' id='loginForm' autocomplete='off'>
            <p>
                <label class="left" for='username'>Username (ie, mhass):</label>
                <input type='text' class='text_' name='j_username' id='username'/>
            </p>

            <p>
                <label class="left" for='password'><g:message code="springSecurity.login.password.label"/>:</label>
                <input type='password' class='text_' name='j_password' id='password'/>
            </p>

            <p class="remember_me_holder">
                <input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me'
                       checked='checked'/>
                <label for='remember_me'><g:message code="springSecurity.login.remember.me.label"/></label>
            </p>

            <p>
                <button class="btn large primary" type='submit'
                        id="submit">${message(code: "springSecurity.login.button")}</button>
            </p>
        </form>
    </div>
</div>
<script type='text/javascript'>
    <!--
    (function () {
        document.forms['loginForm'].elements['j_username'].focus();
    })();
    // -->
</script>
</body>
</html>
