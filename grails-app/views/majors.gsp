<%@ page import="coursesearch.Department; coursesearch.Term" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <link rel="stylesheet"
          href="${resource(dir: 'libraries', file: 'jquery-ui/css/smoothness/jquery-ui-1.8.16.custom.css')}"/>
    <g:javascript src="../libraries/expander/jquery.expander.min.js"/>
    <g:javascript src="../libraries/jquery-ui/js/jquery-ui-1.8.16.custom.min.js"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#accordion").accordion({ active:false, autoHeight:false, collapsible:true  });

            $('.major .block').expander({
                preserveWords:true
            });
        });
    </script>
    <style type="text/css">
    #accordion h3 {
        font-size: 18px;
    }

    .major {
        background-color: #f9f9fa;
        border: 1px solid #ccc;
        margin: 0 10px 20px 0px;
        font-size: 16px;
        padding: 15px 20px;
        min-width: 300px;
        line-height: 150%;
    }

    .major .block {
        margin: 7px 0;
    }
    </style>
</head>

<body>

<h1>Majors & Minors</h1>


<div id="majors">
    <div class="major">
        <h2>Computer Science</h2>

        <div class="block">
        </div>

        <div class="block">
        </div>
    </div>
</div>

</body>
</html>