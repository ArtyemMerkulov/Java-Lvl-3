package homework4;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class Tasks {

    private final int N_ITER = 5;
    private final String[] strSeqForTask1 = {"A", "B", "C"};
    private volatile String currStr = strSeqForTask1[0];
    private volatile StringBuilder resStr = new StringBuilder();

    public void task1() {
        resStr.setLength(0);

        ArrayList<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < strSeqForTask1.length - 1; i++) {
            int finalI = i;
            threadList.add(new Thread(() -> foo(strSeqForTask1, finalI, N_ITER)));
        }
        threadList.add(new Thread(() -> foo(strSeqForTask1, strSeqForTask1.length - 1, N_ITER)));

        for (Thread thread : threadList) thread.start();
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(resStr);
    }

    public void task2() {
        new MFU();
    }

    private synchronized void foo(String[] strSeq, int strPos, int nIter) {
        int i = 0;
        while (true) {
            if ((resStr.length() == 0 && strPos == 0) || StringUtils.endsWith(resStr,
                    strSeq[strPos != 0 ? strPos - 1 : strSeq.length - 1])) {
                resStr.append(strSeq[strPos]);
                currStr = strSeq[strPos != strSeq.length - 1 ? strPos + 1 : 0];
                i += 1;
            }

            notifyAll();

            if (i == nIter) break;

            try {
                while (!currStr.equals(strSeq[strPos])) wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
