<html>
<head>
    <title>Kangaroo Error</title>

    <g:javascript src="errorReporting.js"/>
    <style type="text/css">
    body {
        font-family: Lucida Grande, Verdana, Sans-serif;
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

    #bugTitle {
        color: #aaa;
        margin: 7px 0 5px 0;
        text-align: center;
    }

    h1 {
        padding-top: 10px;
        font-size: 20px;
        border-bottom: 1px solid #ccc;
        text-align: center;
        margin-bottom: 20px;
    }

    #submitStatus {
        border-radius: 5px;
        background-color: #eef;
        border: 1px solid #ccf;
        color: #444;
        margin: 0 auto 0 auto;
        padding: 7px 15px 9px 15px;
        font-size: 14px;
        width: 450px;
        text-align: center;
        display: none;
    }

    #submitStatus .header {
        padding-left: 5px;
    }

    #scoutResponse {
        border-radius: 5px;
        background-color: #dfb;
        border: 1px solid #ddd;
        color: #111;
        margin: 15px auto 0px auto;
        padding: 7px 15px 15px 15px;
        width: 450px;
        text-align: center;
        display: none;
    }

    #scoutResponse h4 {
        font-size: 14px;
        color: #333;
        border-bottom: 1px solid #bdb;
        margin-bottom: 5px;
    }

    #extraInfo {
        width: 450px;
        padding: 10px 15px 20px 20px;
        border-radius: 5px;
        background-color: #f0f0f0;
        border: 1px solid #ccc;
        margin: 35px auto 30px auto;
        display: none;
    }

    #extraInfo textarea {
        width: 425px;
        display: block;
        margin: 0px auto 12px auto;
    }

    #extraInfo #addReportSubheader {
        text-align: center;
        line-height: 28px;
        margin-bottom: 8px;
    }

    #addReportSubheader b {
        color: #999;
    }

    #extraInfo h3 {
        font-size: 16px;
        text-align: center;
        /*margin: 22px 0 0 0 0;*/
        padding-top: 10px;
        line-height: 12px;
    }

    #extraInfo label {
        font-weight: bold;
        width: 450px;
        margin-bottom: 10px;
        text-align: left;
        font-size: 14px;
        color: #999;
    }

    </style>

    <script type="text/javascript">
        $(document).ready(initProductionErrorReporting);
    </script>
</head>

<body>
<h1 style="text-align:center;">
    <img src="${createLinkTo(dir: "images", file: "error_delete.png")}"/> We're sorry &mdash; Kangaroo has encountered an error.
</h1>

<div id="submitStatus">
    <div class="header">Submitting bug report...</div>

    <div id="progress"><img src="${resource(dir: 'images', file: 'ajax-progressbar.gif')}"/></div>

    <div id="submitDiagnostics"></div>
</div>

<div id="scoutResponse">
    <h4>A response from the Kangaroo team:</h4>

    <div id="message"></div>
</div>

<div id="extraInfo">
    <h3>What were you doing when the error occurred?</h3>

    <div id="addDetailFields" style="padding-top: 4px">

        <div id="addReportSubheader"><b>Optional</b> &mdash; this will help us fix this bug faster.</div>

        <div><textarea id="reportDetails" cols="50" rows="4"></textarea></div>

        %{--<g:if test="${!hasEmail}">--}%
        %{--<div style="margin-top: 5px;">--}%
        %{--<label for="userEmail">Your e-mail (optional). We'll follow up when the bug is fixed.</label>--}%

        %{--<div><input type="text" id="userEmail"/></div>--}%
        %{--</div>--}%
        %{--</g:if>--}%

        <div style="margin-top: 15px;"><button id="submitExtraInformation"
                                               class="btn large primary">Send &raquo;</button></div>
    </div>
</div>

<div id="bugTitle">${bugTitle}...&nbsp; <a href="#" id="showDetails">show details &raquo;</a></div>

<div id="errorDetailsContainer">

    <h3>Error Details</h3>

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
            <h3>Stack Trace</h3>

            <div class="stack">
                <pre><g:each in="${exception.stackTraceLines}">${it.encodeAsHTML()}<br/></g:each></pre>
            </div>
        </g:if>
    </div>
    <br/>
</div>
</body>
</html>