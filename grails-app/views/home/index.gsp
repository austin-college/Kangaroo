<%@ page import="kangaroo.Requirement; kangaroo.Department; kangaroo.Term" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'data_table.css')}"/>
    <r:require modules="search"/>
</head>

<body>

<div id="loading">
    Loading courses...
</div>

<div id="searchArea" class="disabled">

    <div id="searchControls">
        <div id="bigFilterBar">
            <label for="tableSearch">Search for classes:</label>
            <g:textField name="tableSearch"/>
        </div>

        <div id="searchDescription">
            (or professors, majors, meeting times...)
        </div>
    </div>

    <div class="otherControls">
        Show classes from
        <g:select name="terms"
                  from="${Term.list()}"
                  optionKey="id"
                  style="width: 120px"/>

        in
        <g:select name="departments"
                  from="${[[id: "ANY", name: "Any Department"]] + Department.list()}"
                  optionKey="id"
                  optionValue="name"
                  style="width: 150px"/>

        and that satisfy
        <g:select name="requirements"
                  from="${[[id: "ANY", name: "Any Requirement"]] + Requirement.findAllWhere(isInterdisciplinaryMajor: false)}"
                  optionKey="id"
                  optionValue="name"
                  style="width: 160px"/>

    </div>

    <div id="tableHolder">
        <g:render template="emptyTable"/>
    </div>
</div>

</body>
</html>