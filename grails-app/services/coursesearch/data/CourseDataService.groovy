package coursesearch.data

import coursesearch.mn.CourseMeetingTime
import coursesearch.mn.Teaching
import groovy.util.slurpersupport.GPathResult
import java.util.zip.ZipFile
import coursesearch.*

/**
 * Extracts information about courses from saved WebHopper pages.
 */
class CourseDataService {

    static transactional = true

    def downloadAndProcess() {

        print "Parsing courses from WebHopper..."

        // Clear existing data.
        Teaching.list().each { it.delete(flush: true);}
        Course.list().each { it.delete(flush: true);}

        // Download the ZIP containing the scrapes.
        def temporaryFile = new File('scrapes.zip');
        temporaryFile.withOutputStream { stream ->
            stream << new URL('http://phillipcohen.net/scrapes.zip').openStream();
            stream.close();
        }

        // Extract and parse the ZIP.
        def zip = new ZipFile('scrapes.zip')
        int totalSaved = 0;
        int totalErrors = 0;
        zip.entries().each { f ->

            // Parse each file for HTML.
            def file = coursesearch.CourseUtils.cleanAndConvertToXml(zip.getInputStream(f)?.text);
            if (file) {
                def results = parseScrapeFile(file, f.name);
                totalSaved += results.numSaved;
                totalErrors += results.numErrors;
                print "..${totalSaved}.."
            }
        }

        // All done!
        zip.close()
        temporaryFile.delete();
        println "COURSES PARSED!\n${totalSaved} saved (${Teaching.count()} enrollments); ${totalErrors} errors."
    }

    def parseScrapeFile(file, name) {

        // Iterate through all the rows in the table.
        int rowNum = 0;
        int numSaved = 0;
        int numErrors = 0;
        getSectionsTable(file).tbody.tr.each { row ->

            if (rowNum++ > 2) { // First 2 rows are controls, not data.
                if (parseRow(row))
                    numSaved++;
                else
                    numErrors++;
            }
        }

        [numSaved: numSaved, numErrors: numErrors]
    }

    GPathResult getSectionsTable(GPathResult page) {
        page.depthFirst().collect { it }.find { it.name() == "table" && it.@summary == 'Sections' }
    }

    /**
     * Parses a row in the WebHopper table.
     */
    def parseRow(row) {
        def course = new Course(name: row.td[8].text());
        try {
            course.open = (row.td[2].text() == 'Open');

            // Calculate the number of free/used seats.
            def seatsSplit = row.td[3].toString().split('/');
            if (seatsSplit.size() == 2) {
                course.capacity = (seatsSplit[1] as Integer);
                course.seatsUsed = course.capacity - (seatsSplit[0] as Integer);
            }

            course.instructorConsentRequired = (row.td[4] == 'Y');
            course.reqCode = (row.td[5])
            course.zap = row.td[6].toString().length() > 0 ? Integer.parseInt(row.td[6].toString().trim()) : 0;

            // Find the department (or create a new one).
            def departmentString = row.td[7].toString().split('\\*')[0];
            course.department = Department.findByCode(departmentString) ?: new Department(code: departmentString, name: departmentString).save();
            def courseNumber = row.td[7].toString().split('\\*')[1].toString();

            // Some courses are labs
            if (courseNumber.endsWith("L")) {
                course.isLab = true;
                course.courseNumber = (courseNumber[0..-2] as Integer);
            }
            else
                course.courseNumber = (courseNumber as Integer);

            course.section = (row.td[7].toString().split('\\*')[2] as Character);

            // Process the professors (sometimes there are multiple ones).
            def enrollments = [];
            row.td[9].div.input.@value.toString().split('<BR>').each { name ->

                def processedName = CourseUtils.cleanFacultyName(name)

                def professor = Professor.findByName(processedName);
                if (!professor) {
                    professor = new Professor(name: processedName);
                    professor.save()
                }
                enrollments << new Teaching(course: course, professor: professor);
            }

            course.room = row.td[10].toString().trim();

            // Process meeting times.
            List<MeetingTime> meetingTimes = []
            row.td[11].div.input.@value.toString().split('<BR>').each {
                def time = convertMeetingTime(it)
                if (time)
                    meetingTimes << new CourseMeetingTime(course: course, meetingTime: time)
            }

            course.comments = row.td[12].toString();

            if (!course.validate())
                println course.errors.allErrors.join("\n");

            if (!course.save()) {
                println "ERROR in saving ${course}..."
                course.errors.each { error -> println "\t${error}"}
            }
            enrollments.each { it.save(); }
            meetingTimes.each { it.save(); }
            [result: course]
        }
        catch (Exception e) {
            println "Error during course conversion of '${course}'...";
            println e;
        }
    }

    static MeetingTime convertMeetingTime(String composite) {

        if (composite.contains("TBA"))
            return null;

        def days = composite[0..5].trim();
        def time = composite[6..-1].trim()
        def startTime = time.split(" ")[0];
        def endTime = time.split(" ")[1];

        return MeetingTime.findOrCreate(new MeetingTime(days: days, startTime: startTime, endTime: endTime));
    }
}
