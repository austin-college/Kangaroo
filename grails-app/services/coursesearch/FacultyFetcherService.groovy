package coursesearch

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class FacultyFetcherService {

    static transactional = true

    def downloadFaculty() {

        println 'Fetching page HTML...'
//        def facultyPage = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parse("faculty.htm").toString();

//        Tidy tidy = new Tidy(); // obtain a new Tidy instance
//        tidy.setXHTML(true); // set desired config options using tidy setters
//        tidy.parse(new FileInputStream(new File('faculty.htm')), System.out);

        //def nodes = processXml(facultyPage.toString(), "//ul[@class='staffList']/li");
        //def nodes = facultyPage.breadthFirst().findAll {it.'@class' == 'staffList'}[0].ch
//        facultyPage
    }

    def processXml(String xml, String xpathQuery) {
        def xpath = XPathFactory.newInstance().newXPath()
        def builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        def inputStream = new ByteArrayInputStream(xml.bytes)
        def records = builder.parse(inputStream).documentElement
        def nodes = xpath.evaluate(xpathQuery, records, XPathConstants.NODESET)
        nodes.collect { node -> node.textContent }

    }
}
