<%@ page import="kangaroo.Department; kangaroo.Term" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <style type="text/css">
    .setupBox {
        width: 500px;
        margin: 20px auto;
        /*padding-bottom: 25px;*/
        font-size: 14px;
    }

    .setupBox p {
        font-size: 16px;
        line-height: 22px;
        margin-bottom: 25px;
    }
    </style>
</head>

<body>

<div class="setupBox">
    <h1>Setup Kangaroo</h1>

    <p>
        Kangaroo will <b>copy all data</b> from an existing Outback server. This will take a bit.
    </p>

    <div>
        <button class="btn large">Start Import &raquo;</button>
    </div>
</div>

</body>
</html>