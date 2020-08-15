package homework7;

public class Tester {

    @BeforeSuite
    public static void test1() {
        System.out.println("test1");
    }

    @Test(priority = 3)
    public static void test2() {
        System.out.println("test2");
        Assert.assertEquals(1, 2);
    }

    @Test(priority = 1)
    public static void test3() {
        System.out.println("test3");
        Assert.assertEquals(1, 2);
    }

    @Test
    public static void test4() {
        System.out.println("test4");
        Assert.assertEquals(1, 1);
    }

    @Test
    public static void test5() {
        System.out.println("test5");
        Assert.assertEquals(1, 1);
    }

    @Test(priority = 8)
    public static void test6() {
        System.out.println("test6");
        Assert.assertEquals(1, 6);
    }

    @AfterSuite
    public static void test7() {
        System.out.println("test7");
    }
}
