<%@ page import="kangaroo.Term" %>
<g:if test="${professor.coursesTeaching.size() > 0}">
    <div id="coursesTeaching" class="details-block courses-block span6">

        <div>
            <h3>${professor} is teaching:</h3>

            <g:each in="${Term.list().sort { it.getYear() }.reverse()}" var="term">

                <g:set var="courses" value="${professor.coursesTeaching.findAll {it.term.id == term.id}}"/>
                <g:if test="${courses.size() > 0}">
                    <div class="term">
                        <b>${term.fullDescription}</b>
                        <ul>
                            <g:each in="${courses}" var="course">
                                <li><g:link controller="course" action="show"
                                            id="${course.id}">${course}</g:link> (${course.sectionString()})</li>
                            </g:each>
                        </ul>
                    </div>
                </g:if>
            </g:each>
        </div>
    </div>
</g:if>