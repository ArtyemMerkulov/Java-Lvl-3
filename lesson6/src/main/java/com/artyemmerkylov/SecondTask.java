package com.artyemmerkylov;

public class SecondTask {
    /*
    2. Написать метод, который проверяет состав массива из чисел 1 и 4.
    Если в нем нет хоть одной четверки или единицы, то метод вернет false;
    Написать набор тестов для этого метода (по 3-4 варианта входных данных).
     */
    public static boolean hasOneAndFour(int[] arr) {
        if (arr == null || arr.length < 2) return false;

        boolean isOne = false, isFour = false;

        for (int a : arr) {
            if (a == 1) isOne = true;
            else if (a == 4) isFour = true;

            if (isOne && isFour) return true;
        }

        return false;
    }
}
