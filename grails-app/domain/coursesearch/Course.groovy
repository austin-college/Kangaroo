package coursesearch

class Course {

    def courseListParseService

    boolean open;
    int capacity;
    int seatsUsed;
    boolean instructorConsentRequired;
    String reqCode;
    int zap;

    String department // BIO
    int courseNumber // 652
    char section // A
    boolean isLab = false

    String instructor;
    String name;
    String room;
    String schedule;
    String comments;

    static constraints = {
    }

    String toString() {
        return courseListParseService.courseToString(this)
    }

    String sectionString() {
        return department + ' ' + courseNumber + ' ' + section;
    }
}
