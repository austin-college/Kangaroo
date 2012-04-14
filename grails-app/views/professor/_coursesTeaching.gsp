<%@ page import="kangaroo.Term" %>
<g:if test="${professor.coursesTeaching.size() > 0}">
    <div class="details-block courses-block span5">

        <div>
            <h3>${professor} is teaching:</h3>

            <g:each in="${Term.list()}" var="term">

                <b>${term.fullDescription}</b>
                <ul>
                    <g:each in="${professor.coursesTeaching.findAll {it.term.shortCode == term.shortCode}}" var="course">
                        <li><g:link controller="course" action="show"
                                    id="${course.id}">${course}</g:link> (${course.sectionString()})</li>
                    </g:each>
                </ul>
            </g:each>
        </div>
    </div>
</g:if>