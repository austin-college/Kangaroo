package kangaroo

/**
 * A building on campus.
 */
class Building {

    // The official name of this building ("Moody Science").
    String name

    // A safe, Outback-assigned key for URLs and JSON ("moodyScience").
    String key

    // Optional description.
    String description

    // Optional type (e.g. "Classrooms/Lecture Halls").
    String type

    // Optional full address (street, city, state, zip).
    String address

    // Precise coordinates.
    BigDecimal longitude, latitude

    // Optional URL to a photo of the building.
    String photoUrl

    // Number on the official Austin College map (0 if not used).
    int numberOnMap

    static constraints = {
        key(maxSize: 32, unique: true)
        name(maxSize: 64, unique: true)
        description(maxSize: 512, nullable: true)
        type(nullable: true)
        address(maxSize: 256, nullable: true)
        photoUrl(nullable: true)
        longitude(scale: 16)
        latitude(scale: 16)
    }

    static mapping = {
        columns {
            key(column: "outback_key")
        }
    }

    String toString() { name }

    boolean getIsOnOfficialMap() { return numberOnMap > 0 }

    def toJson() {
        def map = [id: key, name: name, description: description, type: type, photoUrl: photoUrl, address: address,
                longitude: longitude, latitude: latitude, isOnOfficialMap: isOnOfficialMap];

        if (isOnOfficialMap)
            map["numberOnMap"] = numberOnMap

        return map;
    }
}
