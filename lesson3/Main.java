package homework3;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Main {

    public static final String TASKS_FILES_DIR_PATH = "src/homework3/files/";

    public static final int CHARS_PER_PAGE = 1800;

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

        long t0 = System.currentTimeMillis();

        try {
            page = getPage(thirdTaskFilePath, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(page);
        System.out.println("\nLENGTH OF STRING: " + page.length() + " symbols");

        System.out.println("Runtime: " + (System.currentTimeMillis() - t0) + " ms"); // ~25 ms при 200 000 символов

        /*
        Реализация приведенная ниже работает для файла размером ~30 Мб (30 720 000 символов) и числом символов на
        странице 1800 с кодировкой символов UTF8 (не имеет значения используется ли там ASCII-совместимые символы
        или нет) около 5 - 6 минут при первичной индексации. При запросе страницы длинной 1800 символов время
        составляет ~1000 милисекунд. Повторная индексация файла не требуется, так как реализованы методы save и load.

        Подскажите, пожалуйста, как можно оптимизировать индексацию?
        */
        String thirdTaskFilePathTest = TASKS_FILES_DIR_PATH + "8";
        String pageFileReaderPathTest = TASKS_FILES_DIR_PATH + "PageFileReader.ser";

        String firstRes = null, secondRes = null;
        int pageNum = 0;

        long t1 = System.currentTimeMillis();

        try {
            PageFileReader pageFileReaderFirst = new PageFileReader(thirdTaskFilePathTest, CHARS_PER_PAGE);
            PageFileReader.save(pageFileReaderPathTest, pageFileReaderFirst);

            pageNum = new Random().nextInt(pageFileReaderFirst.getTotalPages()) + 1;
            firstRes = pageFileReaderFirst.getPage(pageNum);

            System.out.println("Total pages: " + pageFileReaderFirst.getTotalPages());
            System.out.println("Res string len: " + firstRes.length() + "\nRes string: " + firstRes);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("No such page!");
            e.printStackTrace();
        }

        long t2 = System.currentTimeMillis();

        System.out.println("Runtime first init: " + (t2 - t1) + " ms\n"); // ~228080 ms при 200 000 символов

        try {
            PageFileReader pageFileReaderSecond = PageFileReader.load(pageFileReaderPathTest);

            secondRes = pageFileReaderSecond.getPage(pageNum);

            System.out.println("Total pages: " + pageFileReaderSecond.getTotalPages());
            System.out.println("Res string len: " + secondRes.length() + "\nRes string: " + secondRes);;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("No such page!");
            e.printStackTrace();
        }

        long t3 = System.currentTimeMillis();

        System.out.println("Runtime second init: " + (t3 - t2) + " ms"); // ~75 ms при 200 000 символов

        assert firstRes != null;
        System.out.println("firstRes equals secondRes: " + firstRes.equals(secondRes));
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
