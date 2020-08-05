package homework5;

import homework5.classes.*;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static final int CARS_COUNT = 4;

    public static void main(String[] args) throws InterruptedException {
        /*
        1 Гонка
         */
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        AtomicInteger tunnelLock = new AtomicInteger();
        Race race = new Race(new Road(60), new Tunnel(),
                new Road(40), new Tunnel(), new Road(70));
        Car[] cars = new Car[CARS_COUNT];

        Semaphore smp = new Semaphore(CARS_COUNT / 2);
        CyclicBarrier cb = new CyclicBarrier(cars.length + 1);
        AtomicInteger winnerFlag = new AtomicInteger();

        for (int i = 0; i < cars.length; i++)
            cars[i] = new Car(smp, cb, winnerFlag, tunnelLock, race, 20 + (int) (Math.random() * 10));

        for (int i = 0; i < cars.length; i++)
            new Thread(cars[i]).start();

        try {
            cb.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        try {
            cb.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");

        System.out.println();
        /*
        2 Создать консольный калькулятор с использованием лямбды
        (у калькулятора должно быть 4 функции: сложение, вычитание,
        умножение, деления, достаточно сделать реализацию для двух
        чисел в одной операции)
         */
        System.out.println("!!!START CALCULATOR!!!");
        new Calculator();
    }
}
