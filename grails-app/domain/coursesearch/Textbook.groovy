package coursesearch

class Textbook {


    static belongsTo = [course: Course]

    static transients = ['amazonLink', 'isbn10Digit']

    String isbn

    String title

    String author

    String edition

    String publisher

    String format

    int copyrightYear

    boolean required

    boolean isDigital

    double bookstoreNewPrice
    double bookstoreUsedPrice
    double bookstoreRentalPrice


    boolean matchedOnAmazon
    String imageUrl
    double amazonPrice

    static constraints = {
        isbn(nullable: true)
        edition(nullable: true)
        publisher(nullable: true)
        format(nullable: true)
        imageUrl(nullable: true)
    }

    String toString() { title }

    String getIsbn10Digit() { IsbnConverter.convertTo10Digit(isbn) }

    String getAmazonLink() { "http://www.amazon.com/gp/product/${isbn10Digit}" }
}
