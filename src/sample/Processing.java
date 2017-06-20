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
}