package homework4;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Tasks tasks = new Tasks();

	    /*
	    1. Создать три потока, каждый из которых выводит определенную букву (A, B и C)
	    5 раз (порядок – ABСABСABС). Используйте wait/notify/notifyAll.
	     */
        for (int i = 0; i < 5; i++) {
            tasks.task1();
            Thread.sleep(100);
        }

        /*
        2. Создать модель MFU (c возможность сканирования, печати и ксерокопии)
         */
        tasks.task2();
    }
}
