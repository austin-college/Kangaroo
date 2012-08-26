<g:if test="${professor.coursesTeaching.size() > 0}">
    <div class="details-block courses-block span14">

        <div>
            <h3>Schedule</h3> 

            <div id="calendar"></div>
            
            
        </div>
        
        <g:if test="${professor?.officeNote}">
            <div><b>Note:</b> ${professor?.officeNote.encodeAsHTML()}</div>
        </g:if>
        
        <g:link controller="professor" action="setOfficeHours" id="${professor.id}" >edit</g:link>
        
    </div>
</g:if>