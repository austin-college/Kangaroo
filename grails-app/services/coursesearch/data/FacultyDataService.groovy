package coursesearch.data

import coursesearch.Professor
import groovy.util.slurpersupport.GPathResult
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.SimpleXmlSerializer
import coursesearch.CourseUtils

/**
 * Downloads the faculty page from austincollege.edu, and uses it to augment teacher profiles.
 */
class FacultyDataService {

    static transactional = true

    // These are faculty that have different names on the faculty page (left) than WebHopper (right).
    static def synonyms = [
            "Robert Cape": "Bob Cape",
//            "Renee Countryman": "Renee A. Countryman",
            //            "Peter A. DeLisle": "Peter DeLisle",
            "Daniel Dominick": "Dan Dominick",
//            "Kirk Everist": "Kirk A. Everist",
            "Steven Goldsmith": "Steve Goldsmith",
            "Henry Gorman": "Hank Gorman",
//            "Matthew Hanley": "Matthew A. Hanley",
            //            "Jennifer Johnson": "Jennifer T. Johnson",
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

    def fetchAndMatch() {

        // Download and extract the faculty list.
        println 'Fetching faculty page...'
        def facultyPage = coursesearch.CourseUtils.cleanAndConvertToXml(new URL('http://www.austincollege.edu/faculty-staff/directory/').text);
        def facultyTable = facultyPage.depthFirst().collect { it }.find { it.name() == "ul" && it.@class == 'staffList' }

        // Extract the professor data.
        def scraped = scrapeFaculty(facultyTable);

        // Match what's found to professors already in the system.
        matchScrapedFaculty(scraped)
    }

    List<Map> scrapeFaculty(table) {
        table.depthFirst().collect { it }.findAll { it.name() == "li" }.collect {
            def map = [:];
            map.name = it.h3;
            map.photoUrl = it.h3.a.img.@src
            map.title = it.div[0]
            map.department = it.div[1]
            map.office = it.div[2]
            map.phone = it.div[3]
            map.email = it.div[4]
            return map
        }
    }

    void matchScrapedFaculty(scraped) {

        print 'Matching downloaded faculty...'
        int entriedUsed = 0;
        int entriedUnused = 0;
        scraped.each { s ->
            def cleanName = s.name.toString().replaceAll("Dr. ", "");
            def prof = Professor.findByName(cleanName);

            // Not found - try removing their middle name.
            if (!prof) {
                def firstLast = CourseUtils.cleanFacultyName(cleanName);

                if (firstLast != cleanName && Professor.findByName(firstLast))
                    prof = Professor.findByName(firstLast);
            }

            // Not found - use the synonyms table.
            if (!prof && synonyms.containsKey(cleanName))
                prof = Professor.findByName(synonyms[cleanName]);

            if (prof) {
                prof.matched = true;
                prof.photoUrl = s.photoUrl
                prof.title = s.title
                prof.department = s.department
                prof.office = s.office
                prof.email = s.email
                prof.phone = s.phone
                prof.save();
                entriedUsed++;
            }
            else
                entriedUnused++;
        }

        // Professor.findAllByMatched(false).each { println "Not Matched: ${it}"}
        def matched = Professor.countByMatched(true);
        def percent = ((double) ((matched / Professor.count()) * 100)).round();
        println "...done! ${matched} matched of ${Professor.count()} (${percent}%)."
    }
}
