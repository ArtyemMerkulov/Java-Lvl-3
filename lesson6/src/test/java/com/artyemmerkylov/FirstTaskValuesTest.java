package com.artyemmerkylov;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class FirstTaskValuesTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new int[] {1, 2, 4, 4, 2, 3, 4, 1, 7}, new int[] {1, 7}},
                {new int[] {1, 2, 4, 4, 2, 3, 4, 1, 4}, new int[] {}},
                {new int[] {4, 4, 4, 4, 4, 4, 4, 4, 4}, new int[] {}},
                {new int[] {4, 1, 2, 3, 5, 6, 7, 8, 9}, new int[] {1, 2, 3, 5, 6, 7, 8, 9}},
        });
    }

    private final int[] input;
    private final int[] output;

    public FirstTaskValuesTest(int[] input, int[] output) {
        this.input = input;
        this.output = output;
    }

    @Test
    public void test() {
        Assert.assertArrayEquals(output, FirstTask.getElementsBeforeLast4(input));
    }
}
