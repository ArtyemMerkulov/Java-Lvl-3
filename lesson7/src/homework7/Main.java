package homework7;

import homework7.classes.HomeworkChecker;
import homework7.classes.Snail;
import homework7.classes.TestCore;
import homework7.classes.Tester;

public class Main {

    public static void main(String[] args) {
        /*
        1. Создать класс, который может выполнять «тесты», в качестве тестов выступают
        классы с наборами методов с аннотациями @Test. Для этого у него должен быть
        статический метод start(), которому в качестве параметра передается или объект
        типа Class, или имя класса. Из «класса-теста» вначале должен быть запущен метод
        с аннотацией @BeforeSuite, если такой имеется, далее запущены методы с аннотациями
        @Test, а по завершению всех тестов – метод с аннотацией @AfterSuite. К каждому
        тесту необходимо также добавить приоритеты (int числа от 1 до 10), в соответствии
        с которыми будет выбираться порядок их выполнения, если приоритет одинаковый, то
        порядок не имеет значения. Методы с аннотациями @BeforeSuite и @AfterSuite должны
        присутствовать в единственном экземпляре, иначе необходимо бросить RuntimeException
        при запуске «тестирования».
        Это домашнее задание никак не связано с темой тестирования через JUnit и использованием
        этой библиотеки, то есть проект пишется с нуля.
         */
        TestCore.start(Tester.class);

        System.out.println();

        /*
        2. Написать программу для проверки ДЗ
        (Проанализировать папку с компилированными классами и вызвать методы, проверить результат)
         */
        Object[] answers = new Object[] {
                2.75f,
                Boolean.FALSE,
                Boolean.FALSE,
                Boolean.FALSE,
                "Привет, Петя!",
                Boolean.TRUE
        };

        Object[][] params = new Object[][] {
                {1f, 2f, 3f, 4f},
                {5, 3},
                {-4},
                {0},
                {"Петя"},
                {"2020"}
        };

        HomeworkChecker homeworkChecker = new HomeworkChecker(System.getProperty("user.dir") + "/src/classFiles");
        try {
            homeworkChecker.startCheck(answers, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();

        /*
        3. Написать метод который позволял бы заполнить и вывести на консоль
        матрицу 4 на 4 (заполненую инкрементом числа, начиная с 1)
         */
        new Snail(10);
    }
}
