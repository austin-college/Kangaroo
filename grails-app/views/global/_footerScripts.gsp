%{-- Load async scripts that need to appear before the closing </body>. --}%

%{--------------------------
       FACEBOOK
--------------------------}%

<div id="fb-root"></div>
<script>(function (d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s);
    js.id = id;
    js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=217547714957832";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>

%{--------------------------
        CHARTBEAT
--------------------------}%

<script type="text/javascript">
    var _sf_async_config = { uid:40341, domain:'kangaroo.austincollege.edu' };
    (function () {
        function loadChartbeat() {
            window._sf_endpt = (new Date()).getTime();
            var e = document.createElement('script');
            e.setAttribute('language', 'javascript');
            e.setAttribute('type', 'text/javascript');
            e.setAttribute('src',
                    (("https:" == document.location.protocol) ? "https://a248.e.akamai.net/chartbeat.download.akamai.com/102508/" : "http://static.chartbeat.com/") +
                            "js/chartbeat.js");
            document.body.appendChild(e);
        }

        ;
        var oldonload = window.onload;
        window.onload = (typeof window.onload != 'function') ?
                loadChartbeat : function () {
            oldonload();
            loadChartbeat();
        };
    })();
</script>