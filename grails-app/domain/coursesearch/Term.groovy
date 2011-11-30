package coursesearch

/**
 * Represents a class term at Austin College (ie, "Fall 2012", "January 2015").
 * Terms have classes associated with them.
 */
class Term implements Serializable {

    static hasMany = [courses: Course]
    static transients = ['fullDescription', 'season', 'year']
    static expandedSeasons = ["FA": "Fall", "SP": "Spring", "JA": "January", "SU": "Summer"]

    /**
     * The registrar short code ("11FA", "12SP", "13JA").
     *
     * Everything is derived from this one field!
     */
    String shortCode

    static constraints = {
        shortCode(maxSize: 6, unique: true)
    }

    /**
     * Formats this term nicely (i.e., "Fall 2012").
     */
    String getFullDescription() {
        return "$season $year"
    }

    /**
     * Returns this term's season as a full word (e.g. "Fall", "Spring").
     */
    String getSeason() {
        return expandedSeasons[shortCode[2..-1]];
    }

    /**
     * Returns this term's year (e.g., 2011).
     */
    int getYear() {
        return 2000 + Integer.parseInt(shortCode[0..1]);
    }

    /**
     * Finds or creates the term of the given code.
     */
    static Term findOrCreate(String shortCode) {
        if (Term.findByShortCode(shortCode))
            return Term.findByShortCode(shortCode);
        else
            return new Term(shortCode: shortCode).save();
    }

    String toString() { return fullDescription; }
}
