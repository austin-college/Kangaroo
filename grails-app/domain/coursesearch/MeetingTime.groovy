package coursesearch

class MeetingTime implements Serializable {

    String days

    String startTime

    String endTime

    static mapping = {
//        id(composite: ['days', 'startTime', 'endTime'])
        version(false)
    }

    static constraints = {
    }

    static MeetingTime findOrCreate(MeetingTime properties) {

        // See if this exact meeting time already exists.
        def existing = MeetingTime.withCriteria {
            eq("days", properties.days)
            eq("startTime", properties.startTime)
            eq("endTime", properties.endTime)
        }

        if (existing)
            return existing[0];
        else
            return new MeetingTime(days: properties.days, startTime: properties.startTime, endTime: properties.endTime).save();
    }

    static MeetingTime findOrCreate(String composite) { return findOrCreate(convertFrom(composite)) }

    static MeetingTime convertFrom(String composite) {
        def days = composite[0..5].trim();
        def time = composite[6..-1].trim()
        def startTime = time.split(" ")[0];
        def endTime = time.split(" ")[1];

        return new MeetingTime(days: days, startTime: startTime, endTime: endTime);
    }

    String toString() {
        return "${days} ${startTime} ${endTime}"
    }

    boolean equals(other) {
        if (!(other instanceof MeetingTime)) return false

        return (other.days == days && other.startTime == startTime && other.endTime == endTime)
    }
}
