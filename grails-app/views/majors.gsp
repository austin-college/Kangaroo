<%@ page import="coursesearch.Department; coursesearch.Term" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <link rel="stylesheet"
          href="${resource(dir: 'libraries', file: 'jquery-ui/css/smoothness/jquery-ui-1.8.16.custom.css')}"/>
    <g:javascript src="../libraries/jquery-ui/js/jquery-ui-1.8.16.custom.min.js"/>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#accordion").accordion({ active: false,  autoHeight: false,  collapsible: true  });

            $("a.toggle").live('click', function(event) {

                var expanded = ( $(this).html().indexOf("expand") != -1 );
                if (!expanded) {
                    $(this).html("expand &raquo;");
                    $(this).siblings(".hidden").hide();
                    $(this).siblings(".ellipsis").show();
                } else {
                    $(this).html("&laquo; collapse");
                    $(this).siblings(".hidden").show();
                    $(this).siblings(".ellipsis").hide();
                }
                event.stopImmediatePropagation();
                return false;
            });

        });
    </script>
    <style type="text/css">
    #accordion h3 {
        font-size: 18px;
    }

    .major {
        font-size: 16px;
        padding: 20px;
        border: 1px solid #eee;
        background-color: #f3f3f3;
        line-height: 130%;
    }

    .major .block {
        margin: 7px 0;
    }

    .hidden {
        display: none;
    }

    </style>
</head>

<body>

<h1>Majors & Minors</h1>


<div id="majors">
    <div class="major">
        <h2>Computer Science</h2>

        <div class="block"><strong>A major in computer science</strong> consists of
            <span class="ellipsis">...</span>

            <span class="hidden">
                a minimum of  eight approved computer science course credit units, including the  following core courses: Computer Science 201, 110 (if required), and 120  (if required), 211, and 221. Students must earn a grade of C or above  in each of these core courses. In addition, a major includes approved  computer science elective courses to reach eight or more course credits,  of which two must be numbered 300 or above, and one numbered 400 or  above. Mathematics 120 and 151 also are required.
            </span>
            <a href="#" class="toggle">expand &raquo;</a>
        </div>

        <div class="block"><strong>A minor in computer science</strong> consists of... <a href="#"
                                                                                          class="toggle">expand &raquo;</a>
        </div>
    </div>
</div>

<br/><br/>

<div id="accordion">

    <jq:accordion title="American Studies (major)">
        <p>A <strong>major in American Studies</strong> consists of a minimum of  eight course credit units, including American Studies 231 (or an  approved substitute) and the following:
        </p>

        <p><strong>One course focused on American art, film, literature and/or music, such as:</strong></p>

        <p style="padding-left: 30px;">Arth 250 – Topics in Art History (depending on topic)<br/>
            Arth 343 – Modern Art and Architecture<br/>
            Eng 250, 251, 252 (depending on topic)<br/>
            Eng 353 – Studies in 19th Century American Literature<br/>
            Eng 363 – Studies in 20th Century American Literature<br/>
            Film 201 – Film Culture (depending on topic)<br/>
            Film 450 – Advanced Topics in Film Studies (depending on topic)<br/>
            Mus 114 – Rock ‘N’ Roll<br/>
            Mus 118 – 20th Century American music<br/>
            Mus 342 – Aspects of 20th Century Music (depending on topic)</p>

        <p><strong>One course focused on American ethnic experience, such as:</strong></p>

        <p style="padding-left: 30px;">Eng 250, 251, 252 (depending on topic)<br/>
            Eng 353 – Studies in 19th Century American Literature (depending on topic)<br/>
            Eng 363 – Studies in 20th Century American Literature (depending on topic)<br/>
            Hist 282 – Civil Rights: 1945 – present<br/>
            Hist 250/350 – Topics in History (depending on topic)<br/>
            Hist 284 – The History of Texas<br/>
            Hist 329 – The Spanish Borderlands<br/>
            Anth/Soc 236 – Anthropology of the City (depending on topic)<br/>
            Anth 362 – Immigration Policy and the Immigrant Experience<br/>
            Anth/Soc 365 – Race &amp; Ethnic Relations<br/>
            GS 250 – Intro to Southwestern Studies</p>

        <p><strong>One course focused on American culture before 1900, such as:</strong></p>

        <p style="padding-left: 30px;">Eng 250, 251, 252 (depending on topic)<br/>
            Eng 353 – 19th Century American literature<br/>
            Hist 162 – U.S. History to 1876<br/>
            Hist 284 – The History of Texas<br/>
            Hist 329 – The Spanish Borderlands<br/>
            Hist 361 – American Colonial History &amp; Revolutionary War Era<br/>
            Hist 363 – Civil War &amp; Reconstruction<br/>
            Hist 364 – United States: Gilded Age and Progressive Era</p>

        <p><strong>One course focused on scientific or social scientific study of American culture, such as:</strong>
        </p>

        <p style="padding-left: 30px;">Anth/Soc 236 – Anthropology of the City (depending on topic)<br/>
            Anth 362 – Immigration Policy and the Immigrant Experience<br/>
            Anth/Soc 365 – Race &amp; Ethnic Relations<br/>
            Psci 110 – American Government<br/>
            Psci 120 – American Political Thought<br/>
            Psci 310 – Congress<br/>
            Psci 311 – The Presidency<br/>
            Psci 315 – Campaigns and Elections<br/>
            Psci 410 – U.S. Constitutional Law<br/>
            Psci 432 – U.S. Foreign Policy<br/>
            Soc 121 – Marriage and Family (depending on topic)<br/>
            Soc 241 – Sex and Gender in Society<br/>
            Soc 385 – Social Movements</p>

        <p>The same course may count toward more than one of the above listed  categories, if appropriate (please consult with the director prior to  enrollment). Many departments offer topics courses appropriate to the  above categories in addition to the courses listed here, and these  courses may be included with director’s approval. Of the required  coursework, at least three courses must be numbered 300 or above. It is  strongly recommended that AMST 231 be taken no later than the first semester of the student’s junior year.</p>
    </jq:accordion>

    <jq:accordion title="American Studies (minor)">
        A <strong>minor in American studies</strong> consists of a minimum of  five course credit units, including American Studies 231 (or an  approved substitute) and one course that satisfies each of the four  categories listed for the major (see above). The same course may count  toward more than one of the categories, if appropriate (please consult  with the director prior to enrollment). Of the required coursework, at  least two courses must be numbered 200 or above, and one course must be  numbered 300 or above.
    </jq:accordion>

    <jq:accordion title="Anthropology (minor)">
        A <strong>minor in anthropology</strong> consists of five courses: Anthropology 123 (Introduction to Cultural Anthropology), Anthropology  270 (Research Methods), and three elective courses in anthropology with  at lease one course at the 200 level or higher; and one approved course  outside the discipline in a topic relevant to cultural anthropology.
    </jq:accordion>

    <jq:accordion title="Art (major)">
        A <strong>major in art</strong> consists of a minimum of nine course credit units, including Art 113, 114, and any three approved courses in art history, plus four elective course credit units. Students who plan to attend graduate school in art or who intend to make art a career should take a maximum number of art courses, possibly on a special degree plan. Such students should consult with an art advis0r as early as possible. Students who wish to concentrate in art history may do so within the art major or with a special major plan. Studio courses usually require six hours of laboratory and six hours of individual work per week. The senior art major concentrating in studio art is required to present an exhibition of work during the semester preceding graduation.
    </jq:accordion>

    <jq:accordion title="Art (minor)">
        <strong>A minor in art</strong> consists of a minimum of five course  credit units including Art 113 and any art history course and at least  two courses numbered 200 or above.
    </jq:accordion>

    <jq:accordion title="Computer Science (major)">
        <strong>A major in computer science</strong> consists of a minimum of  eight approved computer science course credit units, including the  following core courses: Computer Science 201, 110 (if required), and 120  (if required), 211, and 221. Students must earn a grade of C or above  in each of these core courses. In addition, a major includes approved  computer science elective courses to reach eight or more course credits,  of which two must be numbered 300 or above, and one numbered 400 or  above. Mathematics 120 and 151 also are required.
    </jq:accordion>

    <jq:accordion title="Computer Science (minor)">
        <strong>A minor in computer science</strong> consists of a minimum of  five approved computer science course credit units, including the  following core courses: Computer Science 201, 110 (if required), and 120  (if required), 211 and 221. Students must earn a grade of C or above in  each of these core courses. A minor also must include one approved  computer science elective courses numbered 300 or above. Interdisciplinary majors and minors also are available.
    </jq:accordion>

    <jq:accordion title="Art History (minor)">
        <strong>A minor in art history</strong> consists of five course  credit units, two of which must be at the 300 level or above. Required  courses include either Art History 231 or 232, which would serve as a  prerequisite for any higher level art history course, and one studio  course, either Art 113 or 114. The Art and Art History Department  encourages the study of art history during an abroad experience, and  upon pre-approval will accept toward the minor up to two courses taken  at other institutions.
    </jq:accordion>

    <jq:accordion title="Math (major)">
        <strong>A major in mathematics</strong> consists of a minimum of  eight course credit units approved by the department, including  Mathematics 251, 252, and five courses numbered 300 or above. Students  planning to major in mathematics are expected to enter directly into the  calculus sequence beginning with Mathematics 151 or 152. They also are  encouraged to take at least one course in computer science beyond the  introductory level. Students planning graduate study should be aware  that some programs require proficiency in German or French.
    </jq:accordion>

    <jq:accordion title="Math (minor)">
        <strong>A minor in mathematics</strong> consists of a minimum of five  course credit units approved by the department, including Mathematics  251, 252, and two numbered 300 or above. Prerequisite courses must be passed with a grade of C or higher. Those  who would major or minor in mathematics must take those courses under  the “grade” option.
    </jq:accordion>

    <jq:accordion title="Sociology (major)">
        <strong>A major in sociology</strong> consists of eight approved  course credit units including Sociology 101, 240, 270, and three courses  numbered 300 or above. All prospective majors are strongly encouraged  to take these required courses as early as possible and to seek faculty  advice to ensure the most effective reflection of student intellectual  and career goals.
    </jq:accordion>

    <jq:accordion title="Sociology (minor)">
        <strong>A minor in sociology</strong> consists of five course credit  units including Sociology 240 and 270 and at least one course numbered  300 or above. Departmental faculty will assist students when selecting  the appropriate courses for the minor. The selection should reflect a  coherent program within sociology as well as possible connections to the  student’s academic major.
    </jq:accordion>

</div>

</body>
</html>