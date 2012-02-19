%{-- Load Analytics (and other async) scripts. --}%

%{--------------------------
     GOOGLE ANALYTICS
--------------------------}%

<script type="text/javascript">

    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-26802912-1']);
    _gaq.push(['_trackPageview']);

    (function () {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();

</script>

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
       MIXPANEL
--------------------------}%

<script type="text/javascript">

    var mpq = [];
    mpq.push(["init", "102753bbc8bcef0e34932d5f829ed00d"]);
    (function () {
        var b, a, e, d, c;
        b = document.createElement("script");
        b.type = "text/javascript";
        b.async = true;
        b.src = (document.location.protocol === "https:" ? "https:" : "http:") + "//api.mixpanel.com/site_media/js/api/mixpanel.js";
        a = document.getElementsByTagName("script")[0];
        a.parentNode.insertBefore(b, a);
        e = function (f) {
            return function () {
                mpq.push([f].concat(Array.prototype.slice.call(arguments, 0)))
            }
        };
        d = ["init", "track", "track_links", "track_forms", "register", "register_once", "identify", "name_tag", "set_config"];
        for (c = 0; c < d.length; c++) {
            mpq[d[c]] = e(d[c])
        }
    })();

    var cookie = getCookie('prof_id');
    if (cookie) {
        mpq.name_tag(cookie);
        mpq.identify(cookie);
    }

</script>