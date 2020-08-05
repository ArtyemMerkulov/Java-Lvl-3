package homework5.classes;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Car implements Runnable {

    private static int CARS_COUNT;

    private Semaphore smp;
    private CyclicBarrier cb;
    private AtomicInteger winnerFlag;

    private Race race;
    private int speed;
    private String name;

    static {
        CARS_COUNT = 0;
    }

    public Car(Semaphore smp, CyclicBarrier cb, AtomicInteger winnerFlag, Race race, int speed) {
        this.smp = smp;
        this.cb = cb;
        this.winnerFlag = winnerFlag;
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public synchronized void waitTunnel(AtomicInteger tunnelLock) {
        synchronized (tunnelLock) {
            tunnelLock.incrementAndGet();
            if (tunnelLock.get() == CARS_COUNT / 2) {
                tunnelLock.notifyAll();
                tunnelLock.set(0);
            } else {
                try {
                    tunnelLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            cb.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            Stage currentStage = race.getStages().get(i);

            if (currentStage.getClass().getName().equals("homework5.classes.Tunnel")) {
                try {
                    smp.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            currentStage.go(this);

            if (currentStage.getClass().getName().equals("homework5.classes.Tunnel")) {
                smp.release();
            }
        }

        if (winnerFlag.get() == 0) {
            winnerFlag.incrementAndGet();
            System.out.println(name + " - WIN");
        }

        try {
            cb.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
