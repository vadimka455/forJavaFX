package sample.Server;



import java.io.File;

public class ServerProcess {
    private ServerSteal steal = new ServerSteal();

    public void start(File file) throws Exception {
        shutdown(file);
        steal.stealPotatoes(file);
    }

    ServerSteal getSteal() {
        return steal;
    }
    public void shutdown(File file){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            new ServerCommands().save(steal.getClubni(), file);
            //new Audio(new File("content/sounds/Exit.wav")).play();
            //try{Thread.currentThread().join(3000);}catch(Exception ignored){}
        }));

    }
}