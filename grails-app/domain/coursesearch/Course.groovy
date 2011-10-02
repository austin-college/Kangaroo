package coursesearch

class Course {

    def courseDataService

    boolean open;
    int capacity;
    int seatsUsed;
    boolean instructorConsentRequired;
    String reqCode;
    int zap;

    Department department // BIO
    int courseNumber // 652
    char section // A
    boolean isLab = false

    String name;
    String room;
    String schedule;
    String comments;

    static constraints = {
    }

    String toString() {
        return name
    }

    String sectionString() { department.code + ' ' + courseNumber + section; }

    List<Professor> getInstructors() { Teaching.findAllByCourse(this)*.professor; }
}
