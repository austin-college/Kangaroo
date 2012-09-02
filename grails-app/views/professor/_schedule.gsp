<g:if test="${professor.coursesTeaching.size() > 0 || professor.getOfficeHours().size() > 0}">
    <div class="details-block courses-block span14">

        <div>
            <h3>Schedule (<b><g:link controller="professor" action="setOfficeHours"
                                     id="${professor.id}">Edit</g:link></b>)</h3>

            <div id="calendar"></div>

        </div>

        <g:if test="${professor?.officeNote}">
            <div><b>Note:</b> ${professor?.officeNote.encodeAsHTML()}</div>
        </g:if>

    </div>
</g:if>