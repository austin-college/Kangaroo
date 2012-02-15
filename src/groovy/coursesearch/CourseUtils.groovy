package coursesearch

import grails.util.Environment
import groovy.util.slurpersupport.GPathResult
import java.security.SecureRandom
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.SimpleXmlSerializer

/**
 * Useful stuff.
 */
public class CourseUtils {

    /**
     * Removes all but someone's first and last name.
     */
    static String cleanFacultyName(String name) {

        // Remove trailing whitespace and "Dr.".
        def processed = name.trim().replaceAll("Dr\\. ", "").trim();

        // Remove any middle initials.
        def words = processed.split(" ");
        if (words.size() == 3 && words[1].length() == 2)
            return words[0] + " " + words[-1];
        else
            processed
    }

    static String extractProfessorUsername(String email, String name) {
        if (email)
            extractProfessorUsername(email)
        else
            extractProfessorUsernameFromName(name)
    }

    static String extractProfessorUsername(String email) {
        if (email) {
            def parts = email.split("@")
            if (parts)
                parts[0]
        }
    }

    static String extractProfessorUsernameFromName(String name) {
        if (name) {
            def parts = cleanFacultyName(name).toLowerCase().split(' ');
            if (parts?.length > 1)
                return parts[0][0] + parts[-1];
        }
    }

    // Our cheap&easy way to parse currency.
    static double parseCurrency(amount) {
        Double.parseDouble(amount[1..-1]);
    }

    static int toPercent(value) {
        (int) ((100.0 * (double) value).round())
    }

    static void runAndTime(String log, Closure toRun) {
        def startTime = System.currentTimeMillis()
        toRun();
        def elapsedTime = (System.currentTimeMillis() - startTime)
        def timeInSeconds = ((double) (elapsedTime / 1000.0)).round(6);

        println "${log}\t in ${timeInSeconds} seconds."
    }

    static double time(Closure toRun) {
        def startTime = System.currentTimeMillis()
        toRun();
        def elapsedTime = (System.currentTimeMillis() - startTime)
        def timeInSeconds = ((double) (elapsedTime / 1000.0)).round(2);
        return timeInSeconds;
    }

    /**
     * Converts the given HTML page into a Groovy-compatible XML tree.
     */
    static GPathResult cleanAndConvertToXml(String html) {

        if (html?.length() == 0)
            return;

        // Clean any messy HTML
        def cleaner = new HtmlCleaner()
        def node = cleaner.clean(html)

        // Convert from HTML to XML
        def props = cleaner.getProperties()
        def serializer = new SimpleXmlSerializer(props)
        def xml = serializer.getXmlAsString(node)

        // Parse the XML into a document we can work with
        return new XmlSlurper(false, false).parseText(xml)
    }

    /**
     * Counts the number of times the given substring appears in the string.
     */
    public static int countSubstringMatches(String string, String substring) {
        if (string.isEmpty() || substring.isEmpty())
            return 0;

        int count = 0;
        int i = 0;
        while ((i = string.indexOf(substring, i)) != -1) {
            count++;
            i += substring.length();
        }
        return count;
    }

    /**
     * [PC] Generates a random token that's base36 encoded. Useful for tokens or authkeys.
     */
    static String generateRandomToken(int bytesOfEntropy = 20) {
        def bytes = new byte[bytesOfEntropy];
        new SecureRandom().nextBytes(bytes);

        // Convert to base 36, which is compact and looks non-threatening to the user.
        return new BigInteger(bytes).abs().toString(36);
    }


    static def findInNode(node, c) { node.depthFirst().collect { it }.find(c)}

    static def findAllInNode(node, c) { node.depthFirst().collect { it }.findAll(c)}

    static String getProfessorLinksForClass(Course course, boolean includeImages, String connector = ' & ') {
        course.instructors.collect { it ->
            def text = "<a href='${createLink(it.id)}' class='professorLink' title='${it.toString().encodeAsHTML()}' rel='${it.id}'>";

            if (includeImages && it.photoUrl)
                text += "<img src='${it.photoUrl}' width='20px' class='profPhoto'/>"

            text += "${it}</a>"
            text;

        }.join(connector)
    }

    static String getScheduleLinksForClass(Course course, String connector = ' & ') {
        course.meetingTimes.collect { "<a href='${createLink('course', 'bySchedule', it.id)}'>${it}</a>"}.join(connector)
    }

    static String getRoomLinksForProfessor(Professor professor, String connector = ', ') {
        professor.activeRooms.collect { room -> "<a href='${createLink('course', 'byRoom', room)}'>${room.trim()}</a>"}.join(connector)
    }

    /*===================================================

        LINK GENERATORS
        In Grails 2.0 these won't be necessary, as the g.createLink() bean is accessible in plain src/ files.

    ===================================================*/

    static String getPrefix() { (Environment.current == Environment.PRODUCTION) ? "" : "/CourseSearch" }

    static String createLink(controller, action, id) { "${prefix}/${controller}/${action}/${id}" }

    static String createLink(controller, id) { "${prefix}/${controller}/${id}"; }

    static String createLink(id) { "${prefix}/${id}"; }
}