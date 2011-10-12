package coursesearch.data

import coursesearch.Course
import coursesearch.Textbook
import groovyx.gpars.GParsPool
import org.hibernate.Hibernate
import org.hibernate.SessionFactory
import coursesearch.CourseUtils

class TextbookDataService {

    static transactional = false

    SessionFactory sessionFactory

    def lookupTextbooksForAllCourses() {

        println "Fetching detailed textbook data from bkstr.com..."
//
        CourseUtils.runAndTime("Textbooks fetched") {
            GParsPool.withPool(100) {
                def courses = Course.list();
                courses.eachParallel { course ->
                    println "[${Thread.currentThread().id}] Looking up course #${course.id} (${course})..."
                    lookupTextbookInfo(course)
                }
            }
        }

        sessionFactory.currentSession.flush()
        sessionFactory.currentSession.clear()

//
        //        def numThreads = 5;
        //        numThreads.times { offset ->
        //            Thread.start {
        //                for (int i = offset; i < Course.count(); i += numThreads) {
        //                    println "Looking up course ${i} (${Course.get(i)})..."
        //                    lookupTextbookInfo(Course.get(i));
        //                }
        //            }
        //        }

//        CourseUtils.runAndTime("Textbooks fetched linearly") {
//            def startTime = System.currentTimeMillis()
//            Course.list().eachWithIndex { course, i ->
//                lookupTextbookInfo(course);
//                if (i % 20 == 0) println "...${i}..."
//            }
//        }
        println "....done!";
    }

    def lookupTextbookInfo(Course course) {

        Course.withTransaction {
            // Download and extract the list of textbooks.
            Textbook.findAllByCourse(course).each { it.delete() }

            course.textbooksParsed = false;

            def page = coursesearch.CourseUtils.cleanAndConvertToXml(new URL(course.textbookPageUrl()).text);
            def list = page.depthFirst().collect { it }.find { it.name() == "div" && it.@class.toString().contains("results") }

            int failures = 0;
            list.depthFirst().collect { it }.findAll { it.name() == "ul" }.collect {

                def textbook = new Textbook(course: course);

                it.li.each { detail ->

                    def line = detail.toString().trim();

                    // Information about the textbooks is presented as "TYPE:<data>".
                    def parts = line.split(":");
                    if (parts.size() >= 2) {
                        def key = parts[0].trim().toUpperCase();
                        def value = parts[1..-1].join(":").trim(); // Join any colons in the value field together.
                        parseTextbookDetail(textbook, key, value);
                    }
                }

                textbook.save();
                if (textbook.hasErrors()) {
                    println textbook.errors.toString()
                    failures++;
                }
            }

            if (failures) {
                course.textbooksParsed = false;
                println "Failed to save ${failures} books for ${course}..."
            }
            else
                course.textbooksParsed = true;

            course.merge()
//            sessionFactory.currentSession.flush()
//            sessionFactory.currentSession.clear()
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
