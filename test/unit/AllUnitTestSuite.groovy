import junit.framework.Test

public class AllUnitTestSuite extends AllTestSuite {

    private static final String BASEDIR = "./test/unit"
    private static final String PATTERN = "**/*Tests.groovy"

    public static Test suite() {
        return suite(BASEDIR, PATTERN)
    }

}