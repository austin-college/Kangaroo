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