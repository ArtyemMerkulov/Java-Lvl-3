package homework4;

import java.util.Arrays;
import java.util.LinkedList;

public class MFU {

    private final Object scanner = new Object();
    private final Object printer = new Object();

    private Thread[] tasksThreads;
    private Thread initThread;

    private LinkedList<String> requestsQueue = new LinkedList<>(Arrays.asList("printing", "scanning",
            "scanning", "printing", "printing", "scanning", "photocopying", "photocopying", "printing", "scanning",
            "photocopying", "photocopying", "photocopying", "scanning", "printing", "scanning", "printing", "scanning"));

    public MFU() {
        tasksThreads = new Thread[2];

        startInitThread();

        try {
            initThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (true)  {
            if (allTaskDone()) break;
        }
    }

    private void startInitThread() {
        initThread = new Thread(() -> {
            while(requestsQueue.size() > 0) {
                int doneThreadIndex = getDoneThreadIndex();

                if (doneThreadIndex != -1 && !hasPhotocopyingThread()) {
                    try {
                        if (requestsQueue.getFirst().equals("photocopying")) {
                            for (Thread taskThread : tasksThreads) {
                                if (taskThread != null && taskThread.isAlive()) {
                                    try {
                                        taskThread.join();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        tasksThreads[doneThreadIndex] = getNewTaskThreadByRequest(requestsQueue.getFirst());
                        tasksThreads[doneThreadIndex].start();

                        requestsQueue.removeFirst();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        initThread.start();
    }

    private boolean allTaskDone() {
        boolean res = requestsQueue.size() == 0;

        for (Thread taskThread : tasksThreads) res = res && !taskThread.isAlive();

        return res;
    }

    private int getDoneThreadIndex() {
        for (int i = 0; i < tasksThreads.length; i++)
            if (tasksThreads[i] == null || !tasksThreads[i].isAlive()) return i;

        return -1;
    }

    private boolean hasPhotocopyingThread() {
        for (Thread taskThread : tasksThreads)
            if (isPhotocopyingThread(taskThread)) return true;

        return false;
    }

    private boolean isPhotocopyingThread(Thread taskThread) {
        if (taskThread == null || !taskThread.isAlive()) return false;

        return taskThread.getName().equals("photocopying");
    }

    private Thread getNewTaskThreadByRequest(String request) {
        Thread newTaskThread = null;

        switch (request) {
            case "printing":
                newTaskThread = new Thread(() -> {
                    try {
                        printing();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "scanning":
                newTaskThread = new Thread(() -> {
                    try {
                        scanning();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "photocopying":
                newTaskThread = new Thread(() -> {
                    try {
                        photocopying();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                break;
        }

        if (newTaskThread != null) newTaskThread.setName(request);

        return newTaskThread;
    }

    private void printing() throws InterruptedException {
        synchronized (printer) {
            System.out.println("Начинаю печать");
            Thread.sleep(2000);
            System.out.println("Оканчиваю печать");
        }
    }

    private void scanning() throws InterruptedException {
        synchronized (scanner) {
            System.out.println("Начинаю сканировать");
            Thread.sleep(1000);
            System.out.println("Оканчиваю сканировать");
        }
    }

    private void photocopying() throws InterruptedException {
        System.out.println("Начинаю ксерокопировать");
        Thread.sleep(3000);
        System.out.println("Оканчиваю ксерокопировать");
    }
}
