<html>
<head>
    <title>Kangaroo Error</title>
    <style type="text/css">
    body {
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        padding: 0;
        margin: 0;
    }

    #header {
        padding: 10px;
        background-color: #f2f2f2;
    }

    .message {
        border: 1px solid black;
        padding: 10px;
        line-height: 170%;
        font-size: 14px;
        background-color: #E9E9f3;
    }

    .stack {
        border: 1px solid black;
        padding: 5px;
        overflow: auto;
        height: 300px;
    }

    .snippet {
        padding: 5px;
        background-color: white;
        border: 1px solid black;
        margin: 3px;
        font-family: courier;
    }

    #details {
        clear: both;
        margin-top: 50px;
        margin-left: 50px;
        padding-right: 30px;
    }

    #imagePane {
        float: left;
    }

    #imagePane img {
        border: 1px solid #666;
        margin: 50px;
    }

    #errorPane {
        margin-left: 200px;
        padding: 20px;
        margin-top: 60px;
        font-size: 20px;
        /*float: right;*/
    }

    </style>
</head>

<body>

<div id="header">
    <div id="imagePane">
        <img src="http://i.imgur.com/Onx1t.jpg" alt="Sleeping kangaroo" title="Sad Kangaroo"/>
    </div>

    <div id="errorPane">
        <h1>Oops! Kangaroo had a problem.</h1>

        <div>We're very sorry about this. It will be fixed soon.</div>
    </div>

    <div style="clear: both;"></div>
</div>

<div id="details">

    <h2>Details</h2>

    <div class="message">
        <strong>Error ${request.'javax.servlet.error.status_code'}:</strong> ${request.'javax.servlet.error.message'.encodeAsHTML()}<br/>
        <strong>Servlet:</strong> ${request.'javax.servlet.error.servlet_name'}<br/>
        <strong>URI:</strong> ${request.'javax.servlet.error.request_uri'}<br/>
        <g:if test="${exception}">
            <strong>Exception Message:</strong> ${exception.message?.encodeAsHTML()} <br/>
            <strong>Caused by:</strong> ${exception.cause?.message?.encodeAsHTML()} <br/>
            <strong>Class:</strong> ${exception.className} <br/>
            <strong>At Line:</strong> [${exception.lineNumber}] <br/>
            <strong>Code Snippet:</strong><br/>

            <div class="snippet">
                <g:each var="cs" in="${exception.codeSnippet}">
                    ${cs?.encodeAsHTML()}<br/>
                </g:each>
            </div>
        </g:if>
    </div>
    <g:if test="${exception}">
        <br/><br/>
        <h2>Stack Trace</h2>

        <div class="stack">
            <pre><g:each in="${exception.stackTraceLines}">${it.encodeAsHTML()}<br/></g:each></pre>
        </div>
    </g:if>
</div>
</body>
</html>