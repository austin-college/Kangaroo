<html>
<head>
    <title>Kangaroo Error</title>
    <meta name="layout" content="main"/>
    <g:javascript src="errorReporting.js"/>
    <less:stylesheet name="errorPage"/>
    <less:scripts/>

    <script type="text/javascript">
        var sourceUrl = "${request.forwardURI - request.contextPath}";
        $(document).ready(initDevelopmentErrorReporting);
    </script>
</head>

<body>
<h1>
    <img src="${createLinkTo(dir: "images", file: "error_delete.png")}"/> &nbsp;Kangaroo has encountered an error.
</h1>

<div id="inDevMode">You're in development mode. A bug report won't be sent unless you push "send" below.</div>

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
    <h3>Submit report to FogBugz</h3>

    <div id="addDetailFields" style="padding-top: 4px">

        <div id="addReportSubheader"><b>Optionally,</b> add details into the box. (What were you doing?)</div>

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
</body>
</html>