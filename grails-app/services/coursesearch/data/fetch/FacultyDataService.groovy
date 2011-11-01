package coursesearch.data.fetch

import coursesearch.CourseUtils
import coursesearch.Professor
import groovy.util.slurpersupport.GPathResult

/**
 * Downloads the faculty page from austincollege.edu, which contains useful data like photos, titles, phone numbers, etc.
 * This data is used to augment existing teacher profiles.
 */
class FacultyDataService {

    static transactional = true

    // These are faculty that have different names on the faculty page (left) than WebHopper (right).
    static def synonyms = [
            "Robert Cape": "Bob Cape",
            "Daniel Dominick": "Dan Dominick",
            "Steven Goldsmith": "Steve Goldsmith",
            "Henry Gorman": "Hank Gorman",
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

    /**
     * Returns the Austin College faculty page as raw text.
     */
    static String getFacultyPageAsText() { new URL('http://www.austincollege.edu/faculty-staff/directory/').text }

    /**
     * Returns the Austin College faculty page as a cleaned Groovy XML tree.
     */
    static GPathResult getFacultyPageAsXml() {CourseUtils.cleanAndConvertToXml(facultyPageAsText)}

    /**
     * Returns just the list of faculty as a cleaned Groovy XML tree.
     */
    static GPathResult getFacultyList() { facultyPageAsXml.depthFirst().collect { it }.find { it.name() == "ul" && it.@class == 'staffList' } }

    def fetchAndMatch() {

        // Download and extract the faculty list.
        CourseUtils.runAndTime("Faculty fetched and matched") {
            println 'Fetching faculty page...'
            matchScrapedFaculty(extractRawData())
        }
    }

    /**
     * Converts the raw XML into a useful model (a list of string maps).
     * Example: [["name": "Aaron Block", "title": "Assistant Professor"], ["name":"Micheal Higgs", title: "ACJavatron"], ...]
     */
    static List<Map> extractRawData() {
        facultyList.depthFirst().collect { it }.findAll { it.name() == "li" }.collect {
            def map = [:];
            map.name = it.h3; // yum!
            map.photoUrl = it.h3.a.img.@src
            map.title = it.div[0]
            map.department = it.div[1]
            map.office = it.div[2]
            map.phone = it.div[3]
            map.email = it.div[4]

            // Convert the map values from Nodes to strings.
            def stringMap = [:]
            map.each { key, value -> stringMap[key] = value.toString() }
            return stringMap;
        }
    }

    /**
     * Takes the processed data from the faculty list and (tries to) match it to the professors in the database.
     */
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
        def percent = (Professor.count() > 0) ? ((double) ((matched / Professor.count()) * 100)).round() : 0;
        println "...done! ${matched} matched of ${Professor.count()} (${percent}%)."
    }
}
