package kangaroo.data.fetch

import kangaroo.CourseUtils
import kangaroo.Textbook
import groovyx.gpars.GParsPool

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

        Textbook.withTransaction {
            try {
                textbook = textbook.merge()
                println "Looking up Amazon.com details for ${textbook} (${textbook.id})..."
                def page = CourseUtils.cleanAndConvertToXml(new URL(textbook.amazonLink).text)

                textbook.matchedOnAmazon = true;
                textbook.amazonPrice = CourseUtils.parseCurrency(CourseUtils.findInNode(page) { it.@id == "actualPriceValue" }.b.toString());
                textbook.imageUrl = CourseUtils.findInNode(page) { node -> node.@id == "prodImageCell" }.a.img.@src;
                if (!textbook.imageUrl)
                    textbook.imageUrl = CourseUtils.findInNode(page) { node -> node.@id == "prodImageCell" }.img.@src;
                textbook.save(flush: true);
                cleanUpGorm()
            }
            catch (Exception e) { println "Failed (${e})" }
        }
    }

    def cleanUpGorm() {
        def session = sessionFactory.currentSession
        session.flush()
        session.clear()
        propertyInstanceMap.get().clear()
    }
}
