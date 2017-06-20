package sample.Server;


import sample.Entity.Potatoes;

import java.io.*;
import java.util.LinkedList;

public class ServerSteal {
    private LinkedList<Potatoes> clubni = new LinkedList<>();

    LinkedList<Potatoes> getClubni() {
        return clubni;
    }

    void setClubni(LinkedList<Potatoes> clubni) {
        this.clubni = clubni;
    }

    void stealPotatoes(File file) throws Exception {
        Thread loadObject = new Thread(() -> {
            String filename = file.getPath();
        });
        loadObject.start();
        try {
            loadObject.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}