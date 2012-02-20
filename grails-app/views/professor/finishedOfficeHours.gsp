<%@ page import="kangaroo.Term; kangaroo.AppUtils; kangaroo.Course" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${professor}: Office Hours Set</title>
    <meta name="layout" content="main"/>
    <less:stylesheet name="profiles"/>

    <style type="text/css">
    .profileLink {
        display: inline-block;
        font-weight: bold;
        padding: 10px 10px;
        margin: 15px 0px;
        border: 1px dotted #ddd;
    }

    ul li {
        color: #333;
        padding: 3px 0;
    }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $("a#addAsBookmark").click(function () {

                var title = "${professor} - Set Office Hours";
                if (navigator.userAgent.toLowerCase().indexOf('chrome') > -1)
                    alert("Press Ctrl+D to bookmark this page.");
                else if (navigator.userAgent.toLowerCase().indexOf('firefox') > -1)
                    alert("Press Ctrl+D to bookmark this page.");
                else if (window.sidebar) // Mozilla Firefox Bookmark
                    window.sidebar.addPanel(location.href, title, "");
                else if (window.external) // IE Favorite
                    window.external.AddFavorite(location.href, title);
                else if (window.opera && window.print) { // Opera Hotlist
                    this.title = document.title;
                    return true;
                } else
                    alert("Press Ctrl+D to bookmark this page.");

                return false;
            });
        });
    </script>
</head>

<body>

<ul class="breadcrumb">
    <li><g:link controller="home">Professors</g:link> <span class="divider">/</span></li>
    <li class="active">${professor}</li>
</ul>

<div class="details-block professor-block span14">

    <g:if test="${professor.photoUrl}">
        <div class="span1" style="margin-right: 25px; padding-top: 10px">
            <img src="${resource(dir: 'images', file: 'tick.png')}"/>
        </div>
    </g:if>


    <div class="span12 info">
        <g:if test="${professor.id == 'dwilliams'}">
            <h1>All done, E. Don!</h1>
        </g:if>
        <g:else>
            <h1>All done, ${professor.firstName}!</h1>
        </g:else>

        <div>Your office hours have been saved.</div>
        <hr/>

        <div>
            <ul>
                <li><b><g:link action="printWeeklyCalendar" id="${professor.id}">Print a weekly calendar</g:link></b>
                    for your office door.</li>
                <li>Check out <g:link action="show" id="${professor.id}"
                                      target="_blank">your shiny Kangaroo page</g:link>.</li>
                <li><g:link action="setOfficeHours"
                            id="${professor.privateEditKey}">Edit your office hours</g:link> again (in case you made a mistake)</li>
            </ul>
        </div>

        <div>
            <b>We strongly recommend <a href="#" id="addAsBookmark">bookmarking this page</a>.
            </b> (So you can easily edit your office hours any time during the semester.)
        </div>

        <br/>

        %{--<div><b>Syllabi:</b> You can put a link to <g:link action="show" id="${professor.id}" target="_blank">your Kangaroo page</g:link> in your syllabi, and students will always--}%
        %{--be able to easily find your contact details and office hours. Just copy and paste this link:</div>--}%

        %{--<div>--}%


        %{--<span class="profileLink">--}%


        %{--<div><a href="http://kangaroo.austincollege.edu/${professor.id}">kangaroo.austincollege.edu/${professor.id}</a></div>--}%
        %{--<div><a href="${g.createLink(action: 'show', id: professor.id)}"><g:createLink action="show" id="${professor.id}" absolute="true"/></a></div></div>--}%
        %{--</span>--}%
        %{--</div>--}%
    </div>
</div>

</body>
</html>