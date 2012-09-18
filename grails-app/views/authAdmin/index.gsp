<html>
<head>
    <meta name='layout' content='main'/>
    <title>Admin Authenticate</title>
    <r:require module="login"/>
    <style type="text/css">
    .dialog label {
        width: 30px;
        float: none;
    }

    button {
        margin-top: 15px;
    }
    </style>
</head>

<body>

<div class="dialog">
    <div class="header">
        Please enter the administration key.
    </div>

    <div class="body">
        <g:if test='${flash.message}'>
            <div class="alert-message block-message error">${flash.message}</div>
        </g:if>

        <g:form class="adminAuthForm" action="authenticate" autocomplete='off'>
            <p>
                <label class="left" for='key'>Key:</label>
                <input type='password' class='text_' name="key" id="key"/>
            </p>

            <p>
                <button class="btn large primary" type='submit'
                        id="submit">Authenticate</button>
            </p>
        </g:form>
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
