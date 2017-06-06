package sample;


import java.io.File;

class Processing {
     private Steal steal = new Steal();

     void start(File file) throws Exception {
        steal.stealPotatoes(file);
    }

     Steal getSteal() {
        return steal;
    }
     void shutdown(File file){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            new Commands().save(steal.getClubni(), file);
            //new Audio(new File("content/sounds/Exit.wav")).play();
            //try{Thread.currentThread().join(3000);}catch(Exception ignored){}
        }));

    }
}