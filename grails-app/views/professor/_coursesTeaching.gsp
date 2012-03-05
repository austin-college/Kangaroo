<%@ page import="kangaroo.Term" %>
<g:if test="${professor.coursesTeaching.size() > 0}">
    <div class="details-block courses-block span5">

        <div>
            <h3>${professor} is teaching:</h3>

            <b>${Term.findOrCreate("11FA").fullDescription}</b>
            <ul>
                <g:each in="${professor.coursesTeaching.findAll {it.term == Term.findOrCreate('11FA')}}"
                        var="course">
                    <li><g:link controller="course" action="show"
                                id="${course.id}">${course}</g:link> (${course.sectionString()})</li>
                </g:each>
            </ul>


            <b>${Term.findOrCreate("12SP").fullDescription}</b>
            <ul>
                <g:each in="${professor.coursesTeaching.findAll {it.term == Term.findOrCreate('12SP')}}"
                        var="course">
                    <li><g:link controller="course" action="show"
                                id="${course.id}">${course}</g:link> (${course.sectionString()})</li>
                </g:each>
            </ul>
        </div>
    </div>
</g:if>