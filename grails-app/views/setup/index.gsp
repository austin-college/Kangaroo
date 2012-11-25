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

    #importProgress {
        display: none;
        margin: 30px 0;
        font-size: 16px;
        line-height: 20px;
        color: #666;
        font-style: italic;
    }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#startImportButton").click(function () {
//                $("#startImportButton").addClass("disabled");
                $("#importProgress").fadeIn();

                $.ajax({
                    url:contextPath + "/setup/startImport",
                    success:function (response) {
                        refreshStatus()
                    }, error:function (response) {
                        refreshStatus()
                    }
                });
                setInterval(refreshStatus, 100);
            });
        });

        function refreshStatus() {
            $.ajax({
                url:contextPath + "/setup/getStatus",
                cache:false,
                success:function (response) {
                    $("#importProgress").html("<b>" + response.status + "</b>: " + response.text);
                }
            });
        }
    </script>
</head>

<body>

<div class="setupBox">
    <h1>Setup Kangaroo</h1>

    <p>
        Kangaroo will <b>copy all data</b> from an existing Outback server. This will take a bit.
    </p>


    <div id="importProgress">
        Starting import...
    </div>

    <div>
        <button id="startImportButton" class="btn large">Start Import &raquo;</button>
    </div>
</div>

</body>
</html>