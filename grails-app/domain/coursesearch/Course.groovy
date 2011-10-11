package coursesearch

class Course {

    static hasMany = [textbooks: Textbook]

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

    String textbookPageUrl() { "http://www.bkstr.com/webapp/wcs/stores/servlet/booklookServlet?sect-1=A&bookstore_id-1=239&term_id-1=11FA&div-1=&dept-1=${department.code}&course-1=${courseNumber}"}

    String toString() { name }

    String sectionString() { department.code + ' ' + courseNumber + section; }

    List<Professor> getInstructors() { Teaching.findAllByCourse(this)*.professor; }
}
