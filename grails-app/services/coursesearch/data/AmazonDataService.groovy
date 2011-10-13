package coursesearch.data

import coursesearch.Textbook
import coursesearch.CourseUtils

/**
 * Pulls in data from Amazon.com about textbooks.
 */
class AmazonDataService {

    static transactional = true

    def lookupTextbookInfo(Textbook textbook) {

        println "Looking up Amazon.com details for ${textbook}..."
        def page = CourseUtils.cleanAndConvertToXml(new URL(textbook.amazonLink).text)

        textbook.matchedOnAmazon = true;
        textbook.amazonPrice = CourseUtils.parseCurrency(findInNode(page) { it.@id == "actualPriceValue" }.b.toString());
        textbook.imageUrl = findInNode(page) { node -> node.@id == "prodImageCell" }.a.img.@src;
        textbook.save();
    }

    def findInNode(node, c) { node.depthFirst().collect { it }.find(c)}
}
