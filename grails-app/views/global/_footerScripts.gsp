%{-- Load async scripts that need to appear before the closing </body>. --}%

%{--------------------------
        GAUG.ES
--------------------------}%

<script type="text/javascript">
    var _gauges = _gauges || [];
    (function () {
        var t = document.createElement('script');
        t.type = 'text/javascript';
        t.async = true;
        t.id = 'gauges-tracker';
        t.setAttribute('data-site-id', '4f662e5ef5a1f529660001a2');
        t.src = '//secure.gaug.es/track.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(t, s);
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