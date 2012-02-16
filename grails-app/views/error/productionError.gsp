<html>
<head>
    <title>Kangaroo Error</title>
    <meta name="layout" content="main"/>
    <g:javascript src="errorReporting.js"/>
    <less:stylesheet name="errorPage"/>
    <less:scripts/>

    <script type="text/javascript">
        var sourceUrl = "${request.forwardURI - request.contextPath}";
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

<div id="bugTitleExpandable"><span id="bugName">${bugTitle}</span>...&nbsp; <a href="#"
                                                                               id="showDetails">show details &raquo;</a>
</div>

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
            <br/>
            <h4>Stack Trace</h4>

            <div class="stack">
                <pre><g:each in="${exception.stackTraceLines}">${it.encodeAsHTML()}<br/></g:each></pre>
            </div>
        </g:if>
    </div>
    <br/>
</div>
</body>
</html>