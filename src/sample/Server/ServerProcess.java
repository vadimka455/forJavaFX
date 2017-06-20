package sample.Server;


import java.io.File;

public class ServerProcess {
    private ServerSteal steal = new ServerSteal();

    public void start(File file) throws Exception {
        steal.stealPotatoes(file);
    }

    ServerSteal getSteal() {
        return steal;
    }
}