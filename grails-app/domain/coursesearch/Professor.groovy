package coursesearch

class Professor {

    String name

    boolean matched = false
    String photoUrl
    String title
    String department
    String office
    String phone
    String email

    static constraints = {
        photoUrl(nullable: true)
        title(nullable: true)
        department(nullable: true)
        office(nullable: true)
        phone(nullable: true)
        email(nullable: true)
    }

    String toString() { name }


    List<Professor> getColleagues() {

        // EXPENSIVE AND HACKY QUERY
        Set<Professor> colleagues = [];
        activeDepartments.each { dept ->
            Course.findAllByDepartment(dept).each { course ->
                course.instructors.each { instr ->
                    colleagues << instr
                }
            }
        }

        colleagues.remove(this);
        (colleagues as List).sort({a, b -> return a.name.compareTo(b.name)})
    }

    List<Department> getActiveDepartments() {
        def depts = (coursesTeaching*.department as Set);
        depts.remove(Department.findByCode("CI"));

        (depts as List).sort({a, b -> return a.name.compareTo(b.name)});
    }

    List<Course> getCoursesTeaching() { return Teaching.findAllByProfessor(this)*.course }
}
