package coursesearch.data.fetch

import grails.test.GrailsUnitTestCase
import coursesearch.CourseUtils

class FacultyDataUnitTests extends GrailsUnitTestCase {

    def facultyDataService

    def testTextDownload() {
        def text = FacultyDataService.facultyPageAsText;

        // Just do some really basic tests to ensure the right page was fetched.
        assert text.contains("Faculty")
        assert text.contains("Professor of")
        assert CourseUtils.countSubstringMatches(text, "Professor of") > 50
        assert text.contains("Biology")
        assert text.contains("@austincollege.edu")
    }

    def testExtractRawData() {
        def data = FacultyDataService.extractRawData()

        // Run some basic tests on the data.
        assert data.size() > 0
        assert data.findAll { it.title.contains("Professor of") }.size() > 50
        assert data.findAll { it.photoUrl.contains("http://") }.size() > 10
        assert data.findAll { it.email.contains("@austincollege.edu") }.size() > 10

        // Count the number of "Professor of"s in the titles, and make sure it matches the page.
        def numProfessors = data.findAll { it.title.contains("Professor of") }.size()
        assert CourseUtils.countSubstringMatches(FacultyDataService.facultyPageAsText, "Professor of") == numProfessors
    }

}
