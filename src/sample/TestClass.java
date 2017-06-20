package sample;

/**
 * Created by vadim on 06.06.2017.
 */
public class TestClass {
    public static void main(String[] args) {
            Thread thread = new Thread(() -> System.out.println("some"));
            thread.start();
            try {
                thread.wait();
                thread.notify();
                System.out.println("some2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}
