package coursesearch

class Textbook {


    static belongsTo = [course: Course]

    String isbn

    String title

    String author

    int copyrightYear

    double bookstoreNewPrice

    double bookstoreUsedPrice

    double bookstoreRentalPrice

    static constraints = {
    }
}
