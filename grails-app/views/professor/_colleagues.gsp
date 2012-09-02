<g:if test="${professor.colleagues.size() > 0}">
    <div id="relatedProfessors" class="details-block courses-block">

        <div>
            <h3>Related professors:</h3>

            <g:each in="${professor.colleagues}" var="colleague">
                <g:link controller="professor" action="show" id="${colleague.id}">
                    <div class="colleague">

                        <img class="photo" src="${colleague.photoUrl}" alt="${colleague}" title="${colleague}">

                        <h3>${colleague}</h3>
                        Teaches in ${colleague.activeDepartments.join(", ")}

                        <div style="clear: both"></div>
                    </div>
                </g:link>
            </g:each>
        </div>
    </div>
</g:if>