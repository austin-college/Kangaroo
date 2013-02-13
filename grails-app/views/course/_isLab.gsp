<%@ page import="kangaroo.Course" %>
<div class="details-block span15" style="padding-top: 25px; padding-bottom: 25px">

    <div style="float: left">
        <img src="${resource(dir: 'images', file: 'secret_lab_kronk_s.png')}" alt="Kronk"/>
    </div>

    <div style="margin-left: 90px">

        <div style="font-size: 30px; color: #999; font-weight: bold; margin-bottom: 10px">Lab Info</div>

        <g:set var="parent" value="${Course.get(course.term, course.department, course.courseNumber, (char) 'A')}"/>
        <div><b>This is a lab for <g:link action="show" id="${parent.id}">${parent}</g:link>.</b></div>

        <div>A $50 lab fee will be billed to your student account.</div>

    </div>

</div>