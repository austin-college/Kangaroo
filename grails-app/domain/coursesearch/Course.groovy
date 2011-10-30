package coursesearch

import coursesearch.mn.CourseMeetingTime
import coursesearch.mn.Teaching

class Course {

    static hasMany = [textbooks: Textbook]

    def courseDataService

    boolean open;
    int capacity;
    int seatsUsed;
    boolean instructorConsentRequired;
    String reqCode;

    Department department // BIO
    int courseNumber // 652
    char section // A
    boolean isLab = false

    String name;
    String room;
    String comments;

    boolean textbooksParsed

    static mapping = {
        textbooks(sort: "title", order: "asc")
        id(generator: 'assigned')
    }

    static constraints = {
    }

    String textbookPageUrl() { "http://www.bkstr.com/webapp/wcs/stores/servlet/booklookServlet?sect-1=A&bookstore_id-1=239&term_id-1=11FA&div-1=&dept-1=${department.code}&course-1=${courseNumber}"}

    String toString() { name }

    String sectionString() { department.code + ' ' + courseNumber + section; }

    List<Professor> getInstructors() { Teaching.findAllByCourse(this)*.professor; }

    List<MeetingTime> getMeetingTimes() { CourseMeetingTime.findAllByCourse(this)*.meetingTime }
}
