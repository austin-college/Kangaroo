%{-- Like status, but more compact. --}%

<g:if test="${status?.status == 'officeHours'}">
    <div class="status available">
        <img src="${resource(dir: 'images/statusIcons', file: 'available.png')}" alt="Available">
        Office hours
    </div>
</g:if>
<g:elseif test="${status?.status == 'inClass'}">
    <div class="status unavailable">
        <img src="${resource(dir: 'images/statusIcons', file: 'away.png')}" alt="Away">
        Currently teaching ${status.course.sectionString()}
    </div>
</g:elseif>