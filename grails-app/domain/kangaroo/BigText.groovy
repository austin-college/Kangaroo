package kangaroo

class BigText {

    String id
    String description

    static constraints = {
        id(size: 32..32)
        description(maxSize: 16384)
    }

    static mapping = {
        id(column: 'md5', generator: 'assigned')
    }

    static BigText getOrCreate(String description) {
        findOrCreateWhere([id: description.encodeAsMD5(), description: description]).merge()
    }

    String toString() { description }
}
