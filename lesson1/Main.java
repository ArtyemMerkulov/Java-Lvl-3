package homework1;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        /*
        1. Написать метод, который меняет два элемента массива местами.
        (массив может быть любого ссылочного типа);
         */
        String[] arr11 = {"1","2","3","4","5"};
        Integer[] arr21 = {1,2,3,4,5};

        String[] res12 = {"1","2","5","4","3"};
        Integer[] res22 = {1,2,5,4,3};

        swap(arr11, 2, 4);
        swap(arr21, 2, 4);

        System.out.println(Arrays.equals(arr11, res12));
        System.out.println(Arrays.equals(arr21, res22));

        System.out.println();
        /*
        2. Написать метод, который преобразует массив в ArrayList;
         */
        String[] arr31 = {"1","2","3","4","5"};
        Integer[] arr41 = {1,2,3,4,5};

        ArrayList<String> res32 = toArrayList(arr31);
        res32.forEach((n) -> System.out.print(n + " "));

        System.out.println();

        ArrayList<Integer> res42 = toArrayList(arr41);
        res42.forEach((n) -> System.out.print(n + " "));

        System.out.println();
        /*
        3. Большая задача:
        a. Есть классы Fruit -> Apple, Orange;(больше фруктов не надо)
        b. Класс Box в который можно складывать фрукты, коробки условно сортируются по типу фрукта,
        поэтому в одну коробку нельзя сложить и яблоки, и апельсины;
        c. Для хранения фруктов внутри коробки можете использовать ArrayList;
        d. Сделать метод getWeight() который высчитывает вес коробки, зная количество фруктов и
        вес одного фрукта(вес яблока - 1.0f, апельсина - 1.5f, не важно в каких это единицах);
        e. Внутри класса коробка сделать метод compare, который позволяет сравнить текущую коробку с той,
        которую подадут в compare в качестве параметра, true - если их веса равны, false в противном случае
        (коробки с яблоками мы можем сравнивать с коробками с апельсинами);
        f. Написать метод, который позволяет пересыпать фрукты из текущей коробки в другую коробку
        (помним про сортировку фруктов, нельзя яблоки высыпать в коробку с апельсинами), соответственно
        в текущей коробке фруктов не остается, а в другую перекидываются объекты, которые были в этой коробке;
        g. Не забываем про метод добавления фрукта в коробку.
         */
        Box<Orange> firstOrangeBox = new Box();
        Box<Orange> secondOrangeBox = new Box();
        Box<Apple> firstAppleBox = new Box();
        Box<Apple> secondAppleBox = new Box();

        firstOrangeBox.add(new Orange(1));
        firstOrangeBox.add(new Orange(2));
        firstOrangeBox.add(new Orange(3));
        firstOrangeBox.add(new Orange(4));
        firstOrangeBox.add(new Orange(5));

        secondOrangeBox.add(new Orange(1));
        secondOrangeBox.add(new Orange(2));
        secondOrangeBox.add(new Orange(3));
        secondOrangeBox.add(new Orange(4));
        secondOrangeBox.add(new Orange(5));

        firstAppleBox.add(new Apple(1));
        firstAppleBox.add(new Apple(2));
        firstAppleBox.add(new Apple(3));
        firstAppleBox.add(new Apple(4));
        firstAppleBox.add(new Apple(5));

        secondAppleBox.add(new Apple(6));
        secondAppleBox.add(new Apple(7));
        secondAppleBox.add(new Apple(8));
        secondAppleBox.add(new Apple(9));
        secondAppleBox.add(new Apple(10));

        System.out.println("\nfirstOrangeBox equals secondOrangeBox: " + firstOrangeBox.equals(secondOrangeBox));
        System.out.println("firstOrangeBox equals firstAppleBox: " + firstOrangeBox.equals(firstAppleBox));
        System.out.println("firstAppleBox equals firstAppleBox: " + firstAppleBox.equals(firstAppleBox));
        System.out.println("firstAppleBox equals secondAppleBox: " + firstAppleBox.equals(secondAppleBox));

        System.out.println("\nfirstOrangeBox: ");
        for (Orange o : firstOrangeBox.getAllElements()) o.info();
        System.out.println("\nsecondOrangeBox: ");
        for (Orange o : secondOrangeBox.getAllElements()) o.info();
        System.out.println("\nfirstAppleBox: ");
        for (Apple o : firstAppleBox.getAllElements()) o.info();
        System.out.println("\nsecondAppleBox: ");
        for (Apple o : secondAppleBox.getAllElements()) o.info();

        firstAppleBox.shiftTo(secondAppleBox);

        System.out.println("\nfirstAppleBox: ");
        for (Apple o : firstAppleBox.getAllElements()) o.info();
        if (firstAppleBox.getTotalElements() == 0) System.out.println("firstAppleBox is empty!");
        System.out.println("\nsecondAppleBox: ");
        for (Apple o : secondAppleBox.getAllElements()) o.info();
    }

    public static <T> void swap(T[] arr, int srcPos, int dstPos) {
        T tmp = arr[srcPos];
        arr[srcPos] = arr[dstPos];
        arr[dstPos] = tmp;
    }

    public static <T> ArrayList<T> toArrayList(T[] arr) {
        return new ArrayList<>(Arrays.asList(arr));
    }
}
