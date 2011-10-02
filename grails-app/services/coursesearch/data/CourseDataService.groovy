package coursesearch.data

import coursesearch.Course
import coursesearch.Department
import coursesearch.Professor
import coursesearch.Teaching
import groovy.util.slurpersupport.GPathResult
import java.util.zip.ZipFile
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.SimpleXmlSerializer

/**
 * Extracts information about courses from saved WebHopper pages.
 */
class CourseDataService {

    static transactional = true

    def downloadAndProcess() {

        println "Parsing courses from WebHopper..."

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
            def file = readAndConvertToXml(zip.getInputStream(f)?.text);
            if (file) {
                def results = parseScrapeFile(file, f.name);
                totalSaved += results.numSaved;
                totalErrors += results.numErrors;
            }
        }

        // All done!
        zip.close()
        temporaryFile.delete();
        println "COURSES PARSED!\n${totalSaved} saved (${Teaching.count()} enrollments); ${totalErrors} errors."
    }

    GPathResult readAndConvertToXml(String text) {

        if (text?.length() == 0)
            return;

        // Clean any messy HTML
        def cleaner = new HtmlCleaner()
        def node = cleaner.clean(text)

        // Convert from HTML to XML
        def props = cleaner.getProperties()
        def serializer = new SimpleXmlSerializer(props)
        def xml = serializer.getXmlAsString(node)

        // Parse the XML into a document we can work with
        return new XmlSlurper(false, false).parseText(xml)
    }

    def parseScrapeFile(file, name) {

        print "Parsing ${name}..."

        // Iterate through all the rows in the table.
        int rowNum = 0;
        int numSaved = 0;
        int numErrors = 0;
        getSectionsTable(file).tbody.tr.each { row ->

            if (rowNum++ > 2) { // First 2 rows are controls, not data.
                if (parseRow(row))
                    numSaved++;
                else
                    totalErrors++;
            }
        }

        println "...${numSaved} saved!"
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
                def professor = Professor.findByName(name.trim());

                if (!professor) {
                    professor = new Professor(name: name.trim());
                    professor.save()
                }
                enrollments << new Teaching(course: course, professor: professor);
            }

            course.room = row.td[10].toString();
            course.schedule = row.td[11].toString();
            course.comments = row.td[12].toString();
            if (!course.save()) {
                println "ERROR in saving ${course}..."
                course.errors.each { error -> println "\t${error}"}
            }
            enrollments.each { it.save(); }
            [result: course]
        }
        catch (Exception e) {
            println "Error during course conversion of '${course}'...";
            println e;
        }
    }
}