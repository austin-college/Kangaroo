<!DOCTYPE html>
<html>
<head>
    <title><g:layoutTitle default="Course Search"/></title>
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
    <link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.3.0/bootstrap.min.css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'app.css')}"/>


    %{-- Set some global javascript variables while we still have access to the grails closures. --}%
    <script type="text/javascript">
        var contextPath = "${request.contextPath}";
    </script>

    <g:layoutHead/>

    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- Le styles -->
    <style type="text/css">
        /* Override some defaults */
    html, body {
        background-color: #eee;
    }

    body {
        padding-top: 40px; /* 40px to make the container go all the way to the bottom of the topbar */
    }

    .container > footer p {
        text-align: center; /* center align it with the container */
    }

    .container {

    }

        /* The white background content wrapper */
    .content {
        background-color: #fff;
        padding: 20px;
        margin: 0 -20px; /* negative indent the amount of the padding to maintain the grid system */
        -webkit-border-radius: 0 0 6px 6px;
        -moz-border-radius: 0 0 6px 6px;
        border-radius: 0 0 6px 6px;
        -webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .15);
        -moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .15);
        box-shadow: 0 1px 2px rgba(0, 0, 0, .15);
    }

        /* Page header tweaks */
    .page-header {
        background-color: #f5f5f5;
        padding: 20px 20px 10px;
        margin: -20px -20px 20px;
    }

        /* Styles you shouldn't keep as they are for displaying this base example only */
    .content .span10,
    .content .span4 {
        min-height: 500px;
    }

        /* Give a quick and non-cross-browser friendly divider */
    .content .span4 {
        margin-left: 0;
        padding-left: 19px;
        border-left: 1px solid #eee;
    }

    .topbar .btn {
        border: 0;
    }

    </style>

</head>

<body>

<div class="container">

    <div class="content">
        <div class="page-header">
            <g:link controller="home"><img src="${resource(dir: 'images', file: 'app_logo.png')}" alt="CourseSearch"
                                           border="0"/></g:link>
        </div>

        <div class="row">
            <div class="span16">
                <g:layoutBody/>
            </div>

        </div>
    </div>

    <footer>
        <p>Created by Phillip</p>
    </footer>

</div> <!-- /container -->

</body>
</html>