<%@ page import="kangaroo.AppUtils" %>
<div class="details-block professor-block span14">

    <g:if test="${professor.photoUrl}">
        <div class="span3 photo">
            <img src="${professor.photoUrl}" alt="${professor}" title="${professor}">
        </div>
    </g:if>

    <div class="span11 info">
        <div style="padding-left: 10px">
            <h1>${professor}</h1>

            <h3>${professor.title}</h3>

            <div id="statusHolder"></div>

            <g:if test="${professor.email}">
                <div><b>E-mail:</b> <a href="mailto:${professor.email}">${professor.email}</a></div>
            </g:if>

            <div><b>Web page:</b> ${AppUtils.createKangarooLink(professor)}</div>

            <g:if test="${professor.office}">
                <div><b>Office:</b> ${professor.office}</div>
            </g:if>

            <g:if test="${professor.phone}">
                <div><b>Phone:</b> ${professor.phone}</div>
            </g:if>

            <div class="printLink">
                <img src="${resource(dir: 'images', file: 'printer.png')}" alt="Print" border="0"/>
                <g:link controller="professor" action="printWeeklyCalendar" id="${professor.id}">Print...</g:link>
            </div>
        </div>
    </div>
</div>