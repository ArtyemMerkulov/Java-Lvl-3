package com.artyemmerkylov;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SecondTaskFalseTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {null},
                {new int[] {}},
                {new int[] {1}},
                {new int[] {4}},
                {new int[] {6}},
                {new int[] {6, 9}},
                {new int[] {6, 9}},
                {new int[] {5, 3, 8, 8, 5, 0, 7, 0, 9}},
                {new int[] {4, 3, 8, 4, 5, 0, 7, 0, 9}},
                {new int[] {1, 3, 8, 1, 5, 0, 7, 0, 9}},
                {new int[] {1, 3, 8, 5, 5, 0, 7, 0, 9}},
                {new int[] {4, 3, 8, 5, 5, 0, 7, 0, 9}},
        });
    }

    private final int[] input;

    public SecondTaskFalseTest(int[] input) {
        this.input = input;
    }

    @Test
    public void testForFalse() {
        Assert.assertFalse(SecondTask.hasOneAndFour(input));
    }
}