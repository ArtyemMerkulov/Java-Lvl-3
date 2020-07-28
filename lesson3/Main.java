package homework3;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {

    public static final String TASKS_FILES_DIR_PATH = "src/homework3/files/";

    public static final int CHARS_PER_PAGE = 200000;

    public static void main(String[] args) {

        /*
        1. Прочитать файл (около 50 байт) в байтовый массив и вывести этот массив в консоль;
         */
        String firstTaskFilePath = TASKS_FILES_DIR_PATH + "1";

        try {
            byte[] firstArr = Files.readAllBytes(Paths.get(firstTaskFilePath));
            System.out.println(Arrays.toString(firstArr));
        } catch (IOException e) {
            System.out.println("No such file: " + firstTaskFilePath);
        }

        System.out.println();

        /*
        2. Последовательно сшить 5 файлов в один (файлы примерно 100 байт). Может пригодиться
        следующая конструкция: ArrayList<InputStream> al = new ArrayList<>(); ...
        Enumeration<InputStream> e = Collections.enumeration(al);
         */
        ArrayList<InputStream> alis = new ArrayList<>();

        try {
            for (int i = 2; i <= 6; i++)
                alis.add(new FileInputStream(TASKS_FILES_DIR_PATH + i));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        SequenceInputStream sis = new SequenceInputStream(Collections.enumeration(alis));

        FileWriter fw = null;
        try {
            fw = new FileWriter(TASKS_FILES_DIR_PATH + "7");

            int c;
            while ((c = sis.read()) != -1) {
                fw.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fw != null;
                fw.close();

                sis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
        Написать консольное приложение, которое умеет постранично читать текстовые файлы
        (размером > 10 mb). Вводим страницу (за страницу можно принять 1800 символов),
        программа выводит ее в консоль. Контролируем время выполнения: программа не должна
        загружаться дольше 10 секунд, а чтение – занимать свыше 5 секунд.
         */
        String thirdTaskFilePath = TASKS_FILES_DIR_PATH + "8";
        String page = null;

        long t1 = System.currentTimeMillis();

        try {
            page = getPage(thirdTaskFilePath, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(page);
        System.out.println("\nLENGTH OF STRING: " + page.length() + " symbols");

        System.out.println("Runtime: " + (System.currentTimeMillis() - t1) + " ms"); // ~25 ms при 200 000 символов
    }

    public static String getPage(String filePath, int pageNum) throws IOException {
        if (pageNum < 1) return null;

        int startPos = CHARS_PER_PAGE * (pageNum - 1);

        if (new File(filePath).length() < startPos) return null;

        byte[] pageBytes = new byte[CHARS_PER_PAGE];

        RandomAccessFile raf = new RandomAccessFile(filePath, "r");

        raf.seek(startPos);
        raf.read(pageBytes, 0, CHARS_PER_PAGE);

        raf.close();

        return new String(pageBytes);
    }
}
