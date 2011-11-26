<%@ page import="coursesearch.Department; coursesearch.Term" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:javascript src="../libraries/expander/jquery.expander.min.js"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $('.major .block').expander({
                preserveWords:true,
                slicePoint: 85
            });
        });
    </script>
    <style type="text/css">
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

    <g:each in="${majorsByDepartment.keySet()}" var="department">
        <div class="major">
            <h2>${department}</h2>
            <g:each in="${majorsByDepartment[department]}" var="major">
                <div class="block">
                    ${major.description}
                </div>
            </g:each>
        </div>
    </g:each>

</div>

</body>
</html>