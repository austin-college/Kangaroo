package kangaroo

/**
 * Represents a class term at Austin College (ie, "Fall 2012", "January 2015").
 * Terms have classes associated with them.
 */
class Term implements Serializable, Comparable<Term> {

    static hasMany = [courses: Course]
    static transients = ['fullDescription', 'season', 'year']
    static expandedSeasons = ["FA": "Fall", "SP": "Spring", "JA": "January", "SU": "Summer"]
    static orderedSeasons = ["JA", "SP", "SU", "FA"]

    String id // The registrar short code ("11FA", "12SP", "13JA") - everything is derived from this one field!

    // Define the current term (for calendars) and default search term (for the search table).
    static final String CURRENT_TERM_CODE = "13SP", DEFAULT_SEARCH_TERM_CODE = "13SP"

    static constraints = {
        id(maxSize: 4)
    }

    static mapping = {
        id(column: 'code', generator: 'assigned')
    }

    // Returns the current term.
    static Term getCurrentTerm() { return Term.get(Setting.getSetting("currentTermCode")) }

    // Returns the current term.
    static Term getDefaultSearchTerm() { return Term.get(Setting.getSetting("defaultSearchTermCode")) }

    /**
     * This term as a nicely formatted string (i.e., "Fall 2012").
     */
    String getFullDescription() { return "$season $year" }

    /**
     * The term's season as a full word (e.g. "Fall", "Spring").
     */
    String getSeason() { return expandedSeasons[this.seasonCode]; }

    /**
     * The term's season as a registrar code (e.g. "FA", "SP").
     */
    String getSeasonCode() { return id[2..-1]; }

    /**
     * All courses in this term.
     */
    List<Course> getCourses() { return Course.findAllByTerm(this) }

    /**
     * The term's calendar year (e.g., 2011).
     */
    int getYear() { return 2000 + Integer.parseInt(id[0..1]); }

    int compareTo(Term other) {
        def score = { Term term ->
            return (term.year * 10) + orderedSeasons.indexOf(term.seasonCode)
        };

        if (other) {
            return score(this) - score(other);
        } else {
            return 1;
        }
    }

    /**
     * Finds or creates the term of the given code.
     */
    static Term findOrCreate(String code) {
        if (Term.exists(code))
            return Term.get(code);
        else
            return new Term(id: code).save(flush: true);
    }

    String toString() { return fullDescription; }

    static def saveFromJsonObject(object) {
        if (Term.get(object.id))
            return Term.get(object.id)

        def term = new Term()
        term.id = object.id;
        return AppUtils.saveSafely(term.save());
    }

    def toJsonObject() { [id: id, description: fullDescription, year: year, season: season, isActive: id == Term.CURRENT_TERM_CODE] }
}
