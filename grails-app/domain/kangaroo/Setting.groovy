package kangaroo

/**
 * Store Kangaroo configuration settings in the database as simple key/value pairs.
 */
class Setting {

    // Unique ID of the setting. (Please use camelCase.)
    String name

    // Value (always a string; client code should parse it)
    String value

    static constraints = {
        name(maxSize: 255, unique: true)
        value(maxSize: 255)
    }

    static String getSetting(String name) {
        return Setting.findByName(name)?.value;
    }

    static void put(String name, String value) {
        withTransaction {
            if (Setting.findByName(name)) {
                def setting = Setting.findByName(name);
                setting.value = value;
                setting.save();
            } else {
                new Setting(name: name, value: value).save();
            }
        }
    }
}
