<%@ page import="kangaroo.Department; kangaroo.Term" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Kangaroo Setup</title>
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

    #importProgress .status {
        font-size: 20px;
        margin: 15px 0;
    }

    #importProgress .li {
        padding: 5px 0;
    }

    .status_succeeded {
        color: #3b7b36;
    }

    .status_running {
        color: #888235;
    }

    .status_failed {
        color: #b3362d;
    }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#startImportButton").click(function () {
                $("#startImportButton").addClass("disabled");
                $("#importProgress").fadeIn();

                $.ajax({
                    url:contextPath + "/setup/startImport",
                    success:function (response) {
                        refreshStatus()
                    }, error:function (response) {
                        refreshStatus()
                    }
                });
                window.setupRefreshInterval = setInterval(refreshStatus, 50);
            });
        });

        function refreshStatus() {
            $.ajax({
                url:contextPath + "/setup/getStatus",
                cache:false,
                success:function (response) {
                    $("#importProgress .status").html("<div class='status_" + response.status + "'>" + response.message + "</div>");
                    $("#importProgress .stages").empty();
                    $.each(response.stages, function (i, stage) {
                        $("#importProgress .stages").append("<li class='status_" + stage.status + "'><b>" + stage.name + "</b>: " + stage.message + "</li>");
                    });

                    // Stop refreshing when the import ends.
                    if (response.status != 'running')
                        clearInterval(window.setupRefreshInterval);
                }
            });
        }
    </script>
</head>

<body>

<div class="setupBox">
    <h1>Setup Kangaroo</h1>

    <p>
        Welcome! Let's get started.
    </p>

    <p>
        To initialize your local database, Kangaroo will <b>copy all data</b> from the existing Outback server. This will take a bit.
    </p>


    <div id="importProgress">
        <div class="status">Running import...</div>
        <ul class="stages">

        </ul>
    </div>

    <div>
        <button id="startImportButton" class="btn large">Start Import &raquo;</button>
    </div>
</div>

</body>
</html>