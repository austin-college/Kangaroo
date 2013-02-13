package kangaroo

class RooRouteStop {

    String name
    String outbackId

    // When is this stop reached (on the hour)
    String time

    // Optional funny description.
    String funnyDescription

    // Precise coordinates.
    BigDecimal longitude, latitude

    static constraints = {
        outbackId(maxSize: 128, unique: true)
        name(maxSize: 128, unique: true)
        funnyDescription(maxSize: 512, nullable: true)
        time(maxSize: 128, nullable: true)
        longitude(scale: 16)
        latitude(scale: 16)
    }

    String toString() { name }

    static RooRouteStop saveFromJsonObject(object) {
        if (RooRouteStop.findByOutbackId(object.id))
            return RooRouteStop.findByOutbackId(object.id)

        return (RooRouteStop) AppUtils.saveSafely(new RooRouteStop(outbackId: object.id, name: object.name, time: object.time,
                funnyDescription: object.funnyDescription, longitude: object.longitude, latitude: object.latitude));
    }

    def toJsonObject() {
        return [id: outbackId, name: name, time: time, description: null, funnyDescription: funnyDescription, longitude: longitude, latitude: latitude];
    }
}
