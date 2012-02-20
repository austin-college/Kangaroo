<%@ page import="kangaroo.AppUtils" %>

<div id="professor_${professor.id}" class="miniBlock">

    <g:if test="${professor.photoUrl}">
        <div class="photo">
            <img src="${professor.photoUrl}" alt="${professor}" title="${professor}" width="80px">
        </div>
    </g:if>

    <div class="info">
        <h1><g:link controller="professor" action="show" id="${professor.id}">${professor}</g:link></h1>

        <h3>${professor.title}</h3>

        <div>
            %{--<g:render template="/professor/shortStatus" model="['status':professor.status]"/>--}%
        </div>
    </div>
</div>