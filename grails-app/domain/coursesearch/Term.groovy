package coursesearch

class Term implements Serializable {

    static transients = ['fullDescription', 'season', 'year']
    static hasMany = [courses: Course]

    // 11FA, 12SP, 13JA
    String shortCode

    static constraints = {
        shortCode(maxSize: 6, unique: true)
    }

//    static mapping = {
    //        id(composite: ['shortCode'])
    //    }

    static Term findOrCreate(String shortCode) {
        if (Term.findByShortCode(shortCode))
            return Term.findByShortCode(shortCode);
        else
            return new Term(shortCode: shortCode).save();
    }

    String toString() { shortCode }

    String getFullDescription() { "$season $year" }

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

    int getYear() { 2000 + Integer.parseInt(shortCode[0..1]); }
}
