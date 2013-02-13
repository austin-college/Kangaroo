package kangaroo

class BigText {

    String id
    String description

    BigText(String description) {
        this.id = description.encodeAsMD5()
        this.description = description;
    }

    static constraints = {
        id(size: 32..32)
        description(maxSize: 16384)
    }

    static mapping = {
        id(column: 'md5', generator: 'assigned')
    }

    static BigText getOrCreate(String description) {
        if (!description)
            return null;

        def id = description.encodeAsMD5();
        if (BigText.get(id))
            return BigText.get(id);
        else
            new BigText(description).save();
    }

    String toString() { description }
}
