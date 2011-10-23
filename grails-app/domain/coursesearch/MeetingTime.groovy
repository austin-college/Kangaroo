package coursesearch

class MeetingTime implements Serializable {

    String days

    String time

    static mapping = {
        id(composite: ['days', 'time'])
        version(false)
    }

    static constraints = {
    }

    static MeetingTime findOrCreate(days, time) {
        def existing = MeetingTime.findByDaysAndTime(days, time);

        if (existing)
            return existing;
        else
            return new MeetingTime(days: days, time: time).save();
    }

    String toString() {
        return days + " " + time;
    }

    boolean equals(other) {
        if (!(other instanceof MeetingTime)) return false

        return (other.days == days && other.time == time)
    }
}
