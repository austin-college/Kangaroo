package coursesearch.data

import coursesearch.Professor
import grails.test.GrailsUnitTestCase

class FacultyDataIntegrationTests extends GrailsUnitTestCase {

    def facultyDataService

    def testFacultyDownload() {

        def positives = [new Professor(name: "Aaron Block"), new Professor(name: "Michael Higgs")]
        def negatives = [new Professor(name: "Albert Einstein")]
        (positives + negatives).each { assert it.save() }

        facultyDataService.fetchAndMatch();

        // The positives should now have titles, e-mails, and photos.
        positives.each { prof ->
            assert prof.matched
            assert prof.title.contains("Professor")
            assert prof.email?.contains("@austincollege.edu")
            assert prof.photoUrl && fetchUrlSafely(prof.photoUrl)
        }

        // Naturally the negatives should not be matched.
        negatives.each { prof -> assert !prof.matched }
    }

    def fetchUrlSafely(url) {
        try {
            return new URL(url).bytes
        }
        catch (Exception e) {
            println "Could not fetch $url: $e"
        }
    }
}
