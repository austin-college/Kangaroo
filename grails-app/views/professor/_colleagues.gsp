<%@ page import="kangaroo.Professor; kangaroo.data.convert.ProfessorService" %>
<g:if test="${professor.relatedProfessors.size() > 0}">
    <div id="relatedProfessors" class="details-block courses-block">

        <div>
            <h3>Related Professors:</h3>

            <g:each in="${professor.relatedProfessors.entrySet()}" var="item">
                <i>${item.key}</i>

                <g:each in="${item.value}" var="colleague">

                    <g:if test="${id != professor.id}">
                        <g:link controller="professor" action="show" id="${colleague.id}">
                            <div class="colleague">

                                <img class="photo" src="${colleague.photoUrl}" alt="${colleague}" title="${colleague}">

                                <h3>${colleague}</h3>
                                Teaches in ${colleague.activeDepartments.join(", ")}

                                <div style="clear: both"></div>
                            </div>
                        </g:link>
                    </g:if>
                </g:each>

            </g:each>
        </div>
    </div>
</g:if>