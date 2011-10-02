package coursesearch.data

import coursesearch.Course
import coursesearch.Department
import coursesearch.Professor
import coursesearch.Teaching
import groovy.util.slurpersupport.GPathResult
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.SimpleXmlSerializer

class CourseDataService {

    static transactional = true

    def downloadAndProcess() {

        println "Parsing courses from WebHopper scrape..."
        Teaching.list().each { it.delete(flush: true);}
        Course.list().each { it.delete(flush: true);}

        int totalSaved = 0;
        int totalErrors = 0;


        def tfile = new File('scrapes.zip').newOutputStream()
        tfile << new URL('http://phillipcohen.net/scrapes.zip').openStream()
        tfile.close()

        def zipFile = new java.util.zip.ZipFile('scrapes.zip')

        zipFile.entries().each { zip ->

            def text = zipFile.getInputStream(zip).text;
            if (text.length() > 0) {
                def file = readAndConvertToXml(text);

                def table = getSectionsTable(file);

                int rowNum = 0;
                int numSaved = 0;
                print "Parsing ${zip.name}..."
                table.tbody.tr.each { row ->

                    if (rowNum++ > 2) {

                        def course = new Course(name: row.td[8].text());
                        try {
                            course.open = (row.td[2].text() == 'Open');

                            def seatsSplit = row.td[3].toString().split('/');
                            if (seatsSplit.size() == 2) {
                                course.capacity = (seatsSplit[1] as Integer);
                                course.seatsUsed = course.capacity - (seatsSplit[0] as Integer);
                            }

                            course.instructorConsentRequired = (row.td[4] == 'Y');
                            course.reqCode = (row.td[5])
                            course.zap = row.td[6].toString().length() > 0 ? Integer.parseInt(row.td[6].toString().trim()) : 0;

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
                            def instructors = row.td[9].div.input.@value.toString()

                            def enrollments = [];
                            instructors.split('<BR>').each { name ->
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
                            numSaved++;
                            totalSaved++;
                        }
                        catch (Exception e) {
                            println "Error during course conversion of '${course}' (table row #${rowNum - 1})...";
                            println e;
                            e.printStackTrace()
                            throw e;
                            totalErrors++;
                            return "ERROR"
                        }
                    }
                }
                println "...${numSaved} saved!"
            }
        }

        new File('scrapes.zip').delete()
        "\nDONE!\n${totalSaved} saved (${Teaching.count()} enrollments); ${totalErrors} errors."
    }

    GPathResult readAndConvertToXml(String text) {
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

    GPathResult getSectionsTable(GPathResult page) {
        page.depthFirst().collect { it }.find { it.name() == "table" && it.@summary == 'Sections' }
    }

    String courseToString(Course c) {
        def str = c.name + " (${c.sectionString()})";
        if (!c.open)
            str += " (closed!)"

        str += " with ${c.seatsUsed} students of ${c.capacity}"

        if (c.instructorConsentRequired)
            str += " (IC required!)"

        str;
    }

}
