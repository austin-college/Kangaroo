package coursesearch

class MajorsController {

    def index = {

        Map majorsByDepartment = [:]

        Department.list().each { dept ->
            def majors = Major.findAllByDepartment(dept);
            if (majors)
                majorsByDepartment.put(dept, majors)
        }

        [majorsByDepartment: majorsByDepartment]
    }
}
