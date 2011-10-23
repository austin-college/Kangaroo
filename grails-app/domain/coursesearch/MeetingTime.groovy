package coursesearch

class MeetingTime implements Serializable {

    String days

    String startTime

    String endTime

    static mapping = {
        id(composite: ['days', 'startTime', 'endTime'])
        version(false)
    }

    static constraints = {
    }

    static MeetingTime findOrCreate(days, startTime, endTime) {

        // See if this exact meeting time already exists.
        def existing = MeetingTime.withCriteria {
            eq("days", days)
            eq("startTime", startTime)
            eq("endTime", endTime)
        }

        if (existing)
            return existing[0];
        else
            return new MeetingTime(days: days, startTime: startTime, endTime: endTime).save();
    }

    String toString() {
        return "${days} ${startTime} ${endTime}"
    }

    boolean equals(other) {
        if (!(other instanceof MeetingTime)) return false

        return (other.days == days && other.startTime == startTime && other.endTime == endTime)
    }
}
