package com.artyemmerkylov;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SecondTaskTrueTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new int[] {1, 4}},
                {new int[] {8, 7, 6, 4, 2, 7, 1}},
                {new int[] {1, 2, 3, 5, 6, 7, 4, 8, 9}},
                {new int[] {4, 3, 8, 4, 5, 4, 7, 1, 1}},
                {new int[] {4, 3, 8, 4, 5, 4, 7, 0, 1}},
        });
    }

    private final int[] input;

    public SecondTaskTrueTest(int[] input) {
        this.input = input;
    }

    @Test
    public void testForTrue() {
        Assert.assertTrue(SecondTask.hasOneAndFour(input));
    }
}