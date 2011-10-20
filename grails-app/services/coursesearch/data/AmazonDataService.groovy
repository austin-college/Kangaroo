package coursesearch.data

import coursesearch.CourseUtils
import coursesearch.Textbook
import groovyx.gpars.GParsPool
import coursesearch.Course

/**
 * Pulls in data from Amazon.com about textbooks.
 */
class AmazonDataService {

    def sessionFactory
    def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
    static transactional = false

    def lookupAllTextbooks() {
        println "Fetching detailed textbook data from amazon.com..."
        CourseUtils.runAndTime("Amazon details fetched") {
            cleanUpGorm()
            GParsPool.withPool(20) {
                Textbook.findAllByMatchedOnAmazon(false).eachParallel { textbook ->
                    lookupTextbookInfo(textbook)
                }
            }
        }
    }

    def lookupTextbookInfo(Textbook textbook) {

        try {
            Textbook.withTransaction {
                textbook = textbook.merge()
                println "Looking up Amazon.com details for ${textbook}..."
                def page = CourseUtils.cleanAndConvertToXml(new URL(textbook.amazonLink).text)

                textbook.matchedOnAmazon = true;
                textbook.amazonPrice = CourseUtils.parseCurrency(findInNode(page) { it.@id == "actualPriceValue" }.b.toString());
                textbook.imageUrl = findInNode(page) { node -> node.@id == "prodImageCell" }.a.img.@src;
                textbook.save(flush: true);
                cleanUpGorm()
            }
        }
        catch (Exception e) { println "Failed (${e})"}
    }

    def findInNode(node, c) { node.depthFirst().collect { it }.find(c)}

    def cleanUpGorm() {
        def session = sessionFactory.currentSession
        session.flush()
        session.clear()
        propertyInstanceMap.get().clear()
    }
}
