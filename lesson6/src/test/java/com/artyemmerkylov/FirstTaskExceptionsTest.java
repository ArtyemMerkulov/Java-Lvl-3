package com.artyemmerkylov;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class FirstTaskExceptionsTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new int[] {0, 1, 2, 3, 5, 6, 7, 8, 9}},
                {new int[] {}},
                {null}
        });
    }

    private final int[] input;

    public FirstTaskExceptionsTest(int[] input) {
        this.input = input;
    }

    @Test(expected = RuntimeException.class)
    public void test() {
        FirstTask.getElementsBeforeLast4(input);
    }
}
