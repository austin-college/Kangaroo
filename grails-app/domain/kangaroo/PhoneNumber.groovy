package kangaroo

/**
 * An important phone number - admissions, campus security, etc.
 */
class PhoneNumber {

    String outbackId

    // Formal name ("Alumni Relations", "President's Office", "Registrar", etc.)
    String name

    // The number (eg "(903) 813-2247"). Not restricted to any particular format.
    // NOTE: Not unique. For example, Career Services, Counseling Services, and Student Services all share the same number.
    String phone

    // Physical address (e.g "Wortham Center\n718 N. Grand Ave.\nSherman, TX 75090")
    String address

    String email

    static constraints = {
        outbackId(maxSize: 64, unique: true)
        name(maxSize: 64, unique: true)
        phone(maxSize: 64)
        address(maxSize: 256, nullable: true)
        email(maxSize: 128, email: true, nullable: true)
    }

    static PhoneNumber saveFromJsonObject(object) {
        if (PhoneNumber.findByOutbackId(object.id)) {
            return PhoneNumber.findByOutbackId(object.id)
        }

        def id = object.id ?: AppUtils.camelCase(object.name)
        return (PhoneNumber) AppUtils.saveSafely(new PhoneNumber(outbackId: id, name: object.name, phone: object.phone, address: object.address, email: object.email));
    }

    def toJsonObject() {
        [id: outbackId, name: name, phone: phone, address: address, email: email]
    }
}
