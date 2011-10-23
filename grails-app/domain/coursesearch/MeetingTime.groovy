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
        def existing = find(properties.days, properties.startTime, properties.endTime);

        if (existing)
            return existing;
        else
            return new MeetingTime(days: properties.days, startTime: properties.startTime, endTime: properties.endTime).save();
    }

    static MeetingTime find(days, startTime, endTime) {
        def list = MeetingTime.withCriteria {
            eq("days", days)
            eq("startTime", startTime)
            eq("endTime", endTime)
        }

        if (list)
            return list[0];
    }

    String toString() {
        return "${days} ${startTime} ${endTime}"
    }

    boolean equals(other) {
        if (!(other instanceof MeetingTime)) return false

        return (other.days == days && other.startTime == startTime && other.endTime == endTime)
    }
}
