package coursesearch

/**
 * Represents a class term at Austin College (ie, "Fall 2012", "January 2015").
 * Terms have classes associated with them.
 */
class Term implements Serializable {

    static hasMany = [courses: Course]
    static transients = ['fullDescription', 'season', 'year']

    // The registrar short code ("11FA", "12SP", "13JA").
    String shortCode

    static constraints = {
        shortCode(maxSize: 6, unique: true)
    }

//    static mapping = {
    //        id(composite: ['shortCode'])
    //    }

    String toString() { fullDescription }

    String getFullDescription() { "$season $year" }

    /**
     * Returns this term's season as a full word (e.g. "Fall", "Spring").
     */
    String getSeason() {
        switch (shortCode[2..-1]) {
            case "FA":
                return "Fall"
            case "SP":
                return "Spring"
            case "JA":
                return "January"
            case "SU":
                return "Summer"
        }
    }

    static Term findOrCreate(String shortCode) {
        if (Term.findByShortCode(shortCode))
            return Term.findByShortCode(shortCode);
        else
            return new Term(shortCode: shortCode).save();
    }

    int getYear() { 2000 + Integer.parseInt(shortCode[0..1]); }
}
