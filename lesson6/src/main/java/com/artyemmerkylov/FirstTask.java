package com.artyemmerkylov;

public class FirstTask {
    /*
    1. Написать метод, которому в качестве аргумента передается не пустой
    одномерный целочисленный массив. Метод должен вернуть новый массив,
    который получен путем вытаскивания из исходного массива элементов,
    идущих после последней четверки. Входной массив должен содержать
    хотя бы одну четверку, иначе в методе необходимо выбросить
    RuntimeException. Написать набор тестов для этого метода (по 3-4
    варианта входных данных). Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
     */
    public static int[] getElementsBeforeLast4(int[] arr) {
        if (arr == null || arr.length == 0) throw new RuntimeException("Invalid array.");

        int pos = arr.length - 1;
        while (pos >= 0) {
            if (arr[pos] == 4) break;
            pos--;
        }

        if (pos < 0) throw new RuntimeException("Array does not contain 4.");

        pos++;

        int totalElementsBefore4 = arr.length - pos;
        int[] res = new int[totalElementsBefore4];

        for (int i = 0; i < totalElementsBefore4; i++) res[i] = arr[pos + i];

        return res;
    }
}
