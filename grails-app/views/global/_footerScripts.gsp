%{-- Load async scripts that need to appear before the closing </body>. --}%

%{--------------------------
       FACEBOOK
--------------------------}%

<script type="text/javascript">

    // prevent jQuery from appending cache busting string to the end of the FeatureLoader URL
    var cache = jQuery.ajaxSettings.cache;
    jQuery.ajaxSettings.cache = true;
    // Load FeatureLoader asynchronously. Once loaded, we execute Facebook init

    jQuery.getScript('http://connect.facebook.net/en_US/all.js', function () {
        FB.init({appId:'your_app_id-optional', status:true, cookie:true, xfbml:true});
    });
    // just Restore jQuery caching setting
    jQuery.ajaxSettings.cache = cache;

</script>

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