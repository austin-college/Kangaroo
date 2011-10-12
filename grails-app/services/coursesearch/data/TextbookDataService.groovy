package coursesearch.data

import coursesearch.Course
import coursesearch.Textbook

class TextbookDataService {

    static transactional = true

    def lookupTextbooksForAllCourses() {
        Course.list().each { course ->
            lookupTextbookInfo(course);
        }
    }


    def lookupTextbookInfo(Course course) {

        // Download and extract the list of textbooks.
        println "Fetching textbook page for ${course}..."
        Textbook.findAllByCourse(course).each { it.delete() }

        def page = coursesearch.CourseUtils.cleanAndConvertToXml(new URL(course.textbookPageUrl()).text);
        def list = page.depthFirst().collect { it }.find { it.name() == "div" && it.@class.toString().contains("results") }

        list.depthFirst().collect { it }.findAll { it.name() == "ul" }.collect {


            def textbook = new Textbook(course: course);

            it.li.each { detail ->

                def line = detail.toString().trim();

                // Information about the textbooks is presented as "TYPE:<data>".
                def parts = line.split(":");
                if (parts.size() >= 2) {
                    def key = parts[0].trim().toUpperCase();
                    def value = parts[1..-1].join(":").trim(); // Join any colons in the value field together.

                    //                    println "${key}---->${value}"
                    parseTextbookDetail(textbook, key, value);
                }
            }

//            println "${textbook.title} by ${textbook.author}"
            textbook.save();
            if (textbook.hasErrors()) println textbook.errors.toString()
        }
    }

    def parseTextbookDetail(Textbook textbook, key, value) {

        switch (key) {
            case 'TITLE':
                textbook.title = value;
                break;

            case 'AUTHOR':
                textbook.author = value;
                break;

            case 'FORMAT':
                textbook.format = value;
                break;

            case 'EDITION':
                textbook.edition = value;
                break;

            case 'COPYRIGHT YEAR':
                def year = safeParseInt(value);
                if (year)
                    textbook.copyrightYear;
                break;

            case 'PUBLISHER':
                textbook.publisher = value;
                break;

            case 'ISBN':
                textbook.isbn = value;
                break;

        // Pricing details...
            case 'NEW':
                textbook.bookstoreNewPrice = parseCurrency(value);
                break;
            case 'USED':
                textbook.bookstoreRentalPrice = parseCurrency(value);
                break;
            case 'RENTAL':
                textbook.bookstoreRentalPrice = parseCurrency(value);
                break;
            case 'DIGITAL':
                textbook.isDigital = true;
                textbook.bookstoreNewPrice = parseCurrency(value);
                break;

            default:
                println "Unused: ${key} (${value})"
        }

    }

    // Our cheap&easy way to parse currency.
    def parseCurrency(amount) {
        Double.parseDouble(amount[1..-1]);
    }

    /**
     * If the input is a number, returns it; otherwise returns null.
     */
    def safeParseInt(value) {
        try {
            Integer.parseInt(value)
        } catch (NumberFormatException) {}
    }


}
