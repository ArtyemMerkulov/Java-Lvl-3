package homework7.classes;

public class Assert {

    public static void assertEquals(String expected, String actual) {
        if (!expected.equals(actual)) {
            fail();
        } else done();
    }

    public static void assertEquals(long expected, long actual) {
        if (expected != actual) {
            fail();
        } else done();
    }

    public static void fail() {
        System.out.println("TEST FAILED!!!!");
    }

    public static void done() {
        System.out.println("TEST PASSED!!!!");
    }
}
