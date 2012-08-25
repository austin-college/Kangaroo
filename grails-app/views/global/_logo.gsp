%{-- Show the logo and header. --}%
<div class="page-header">
    <g:link controller="home">
        <img src="${resource(dir: 'images', file: 'app_logo.png')}" alt="Kangaroo"
             height="58px" width="290px" border="0"/>
    </g:link>

    <g:render template="/global/userLinks"/>
</div>