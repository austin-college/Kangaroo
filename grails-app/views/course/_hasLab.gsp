<div class="details-block span15" style="padding-top: 25px">

    <div style="float: left">
        <img src="${resource(dir: 'images', file: 'secret_lab_kronk_s.png')}" alt="Kronk"/>
    </div>

    <div style="margin-left: 90px">

        <div style="font-size: 30px; color: #999; font-weight: bold; margin-bottom: 10px">Lab Info</div>

        <div><b>This course has a lab.</b> A $50 lab fee will be billed to your student account.</div>

        <div>You will also need to register one of the following lab sections:</div>
        <ul style="padding: 5px 0">
            <g:each in="${course.siblings.findAll { it.isLab }}" var="lab">
                <li style="padding: 3px 0">
                    <b><g:link action="show" id="${lab.id}">${lab.sectionString()}</g:link>:</b>
                    ${lab.meetingTimes.join(" and ")}</li>
            </g:each>
        </ul>

    </div>

</div>