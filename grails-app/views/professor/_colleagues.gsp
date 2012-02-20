<g:if test="${professor.colleagues.size() > 0}">
    <div class="details-block courses-block">

        <div>
            <h3>Colleagues:</h3>

            <ul>
                <g:each in="${professor.colleagues}" var="colleague">
                    <li><g:link controller="professor" action="show"
                                id="${colleague.id}">${colleague}</g:link> (${colleague.activeDepartments.join(", ")})</li>
                </g:each>
            </ul>
        </div>
    </div>
</g:if>