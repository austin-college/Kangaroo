%{-- A call-to-action blurb that encourages professors to set their office hours. --}%
<g:if test="${professor.isActive && professor.officeHours.size() == 0}">
    <div id="pleaseSetOfficeHours" class="alert-message block-message success"
         style="width: 500px; margin-top: 20px; clear: both">
        <b>Are you ${professor}?</b> <g:link controller="professor" action="setOfficeHours"
                                             id="${professor.id}">Set your office hours...
    </g:link>
    </div>
</g:if>