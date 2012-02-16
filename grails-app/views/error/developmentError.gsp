<html>
<head>
    <title>Kangaroo Error</title>

    <g:javascript src="errorReporting.js"/>

    <style type="text/css">
    body {
        font-family: Lucida Grande, Verdana, Sans-serif;
        font-size: 11px;
        line-height: 190%;
        padding-left: 10px;
        padding-right: 10px;
    }

    .message {
        border: 1px solid black;
        padding: 5px;
        background-color: #E9E9E9;
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

    h1 {
        font-size: 16px;
        padding-bottom: 3px;
        border-bottom: 1px solid #ccc;
    }

    #extraInfo {
        margin: 10px 0 30px 0px;
        width: 400px;
        padding: 10px 50px 20px 15px;
        background-color: #eef;
        border: 2px solid #ccd;
    }

    #extraInfo h4 {
        font-size: 14px;
        margin: 0px;
    }

    #extraInfo label {
        font-weight: bold;
        width: 400px;
        margin-bottom: 10px;
        text-align: left;
        font-size: 14px;
        color: #999;
    }

    #reportDetails {
        width: 350px;
    }

    #bugTitle {
        color: #aaa;
        margin-bottom: 5px;
    }

    </style>
</head>

<body>
<h1>
    <img src="${createLinkTo(dir: "images", file: "error_delete.png")}"/> &nbsp;Kangaroo has encountered an error.
</h1>

<div id="submitDiagnostics"></div>

<div id="errorDetailsContainer">

    <div id="errorDetails">
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
            <br/>
            <h4>Stack Trace</h4>

            <div class="stack">
                <pre><g:each in="${exception.stackTraceLines}">${it.encodeAsHTML()}<br/></g:each></pre>
            </div>
        </g:if>
    </div>
    <br/>
</div>

<div id="extraInfo">
    <h4><img src="${createLinkTo(dir: "images", file: "attach.png")}" style="vertical-align: middle;"> <span
            id="reportHeader">Send a report</span></h4>

    <div id="addDetailFields" style="padding: 0 20px">
        <div><label for="reportDetails">What were you doing when the error occurred? <i>(optional)</i></label></div>

        <div><textarea id="reportDetails" cols="50" rows="4"></textarea></div>

        <div style="margin-top: 20px;">
            <button id="submitExtraInformation" class="btn large primary">Send report &raquo;</button>
            <span id="actionLoader"></span>
        </div>
    </div>
</div>
</body>
</html>