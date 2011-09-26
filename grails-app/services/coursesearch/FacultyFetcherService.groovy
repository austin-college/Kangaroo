package coursesearch

import groovy.util.slurpersupport.GPathResult
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.SimpleXmlSerializer

class FacultyFetcherService {

    static transactional = true

    def downloadFaculty() {

        println 'Fetching page HTML...'
        def facultyPage = readAndConvertToXml('faculty.htm')
        def facultyTable = facultyPage.depthFirst().collect { it }.find { it.name() == "ul" && it.@class == 'staffList' }

        def scraped = facultyTable.depthFirst().collect { it }.findAll { it.name() == "li" }.collect {
            def map = [:];
            map.name = it.h3;
            map.photoUrl = it.h3.a.img.@src
            map.title = it.div[0]
            map.division = it.div[1]
            map.location = it.div[2]
            map.phone = it.div[3]
            map.email = it.div[4]
            map
        }

        println "MATCHING...\n";
        def synonyms = [
                "Robert Cape": "Bob Cape",
                "Renee Countryman": "Renee A. Countryman",
                "Peter A. DeLisle": "Peter DeLisle",
                "Daniel Dominick": "Dan Dominick",
                "Kirk Everist": "Kirk A. Everist",
                "Steven Goldsmith": "Steve Goldsmith",
                "Henry Gorman": "Hank Gorman",
                "Matthew Hanley": "Matthew A. Hanley",
                "Jennifer Johnson": "Jennifer T. Johnson",
                "Gregory Kinzer": "Greg Kinzer",
                "Jacqueline Moore": "Jackie Moore",
                "Stephen Ramsey": "Steve Ramsey",
                "Donald Rodgers": "Don Rodgers",
                "Donald Salisbury": "Don Salisbury",
                "Timothy Tracz": "Tim Tracz",
                "Ivette Vargas-O'Bryan": "Ivette Vargas",
                "Michael Wallo": "Michael C. Wallo",
                "Brian Watkins": "Brian A. Watkins",
        ]

        int numMatched = 0;
        int numNotMatched = 0;
        scraped.each { s ->
            def cleanName = s.name.toString().replaceAll("Dr. ", "");
            def prof = Professor.findByName(cleanName);

            if (!prof && synonyms.containsKey(cleanName))
                prof = Professor.findByName(synonyms[cleanName]);

            if (prof) {
                prof.matched = true;
                prof.save();
                numMatched++;
            }
            else
                numNotMatched++;

//            if (!prof)
            //                println cleanName + " / " + prof
        }

        Professor.findAllByMatched(false).each { println "Not Matched: ${it}"}

        println "\nRESULTS: ${numMatched} matched of ${Professor.count()}; ${numNotMatched} not matched"

//        def facultyPage = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parse("faculty.htm").toString();

        //        Tidy tidy = new Tidy(); // obtain a new Tidy instance
        //        tidy.setXHTML(true); // set desired config options using tidy setters
        //        tidy.parse(new FileInputStream(new File('faculty.htm')), System.out);

        //def nodes = processXml(facultyPage.toString(), "//ul[@class='staffList']/li");
        //def nodes = facultyPage.breadthFirst().findAll {it.'@class' == 'staffList'}[0].ch
        //        facultyPage
    }

    GPathResult readAndConvertToXml(String filename) {
        // Clean any messy HTML
        def cleaner = new HtmlCleaner()
        def node = cleaner.clean(new File(filename).text)

        // Convert from HTML to XML
        def props = cleaner.getProperties()
        def serializer = new SimpleXmlSerializer(props)
        def xml = serializer.getXmlAsString(node)

        // Parse the XML into a document we can work with
        return new XmlSlurper(false, false).parseText(xml)
    }

}
