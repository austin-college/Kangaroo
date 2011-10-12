package coursesearch

class Textbook {


    static belongsTo = [course: Course]

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

    static constraints = {
        isbn(nullable: true)
        edition(nullable: true)
        publisher(nullable: true)
        format(nullable: true)
    }
}
