package sample;


import java.io.File;

public class Process {
    static private Steal steal = new Steal();

    public static void start(File file) throws Exception {
        steal.stealPotatoes(file);
    }

    public static Steal getSteal() {
        return steal;
    }
    public static void shutdown(File file){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Commands.save(steal.clubni, file);
            //new Audio(new File("content/sounds/Exit.wav")).play();
            //try{Thread.currentThread().join(3000);}catch(Exception ignored){}
        }));

    }
}