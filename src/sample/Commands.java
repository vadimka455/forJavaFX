package sample;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import sample.Entity.Potatoes;

import java.io.*;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.LinkedList;

import static sample.IntByteOperation.intToByteArray;

class Commands {

    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson = gsonBuilder.create();

    private Type potatoesType = new TypeToken<Potatoes>() {
    }.getType();
    private Connect connect = new Connect();

    void add_if_max(String jsonStr, LinkedList<Potatoes> newclubni) {

        try {
            Potatoes pot = gson.fromJson(jsonStr, potatoesType);
            pot.setZonedDateTime(ZonedDateTime.now());
            Thread add_if_max = new Thread(() -> {
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024 * 1024);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(pot);
                    byte[] type = {(byte) 1};
                    connect.sendPackages(type, byteArrayOutputStream.toByteArray(), intToByteArray(pot.hashCode()));
                    byteArrayOutputStream.close();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }

            });
            add_if_max.start();


        } catch (Exception ignored) {
        }
    }

    void remove_greater(String jsonStr, LinkedList<Potatoes> newclubni) {
        Potatoes pot = gson.fromJson(jsonStr,potatoesType);
        Thread remove_greater = new Thread(() -> {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024 * 1024);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(pot);
                byte[] type = {(byte) 2};
                connect.sendPackages(type, byteArrayOutputStream.toByteArray(), intToByteArray(pot.hashCode()));
                byteArrayOutputStream.close();
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }

        });
        remove_greater.start();
    }

    void remove_last(LinkedList<Potatoes> newclubni) {
        byte[] command = {(byte) 4};
        connect.sendPackages(command, null, null);
    }


    void add_default_elements(LinkedList<Potatoes> clubni) throws Exception {

        Thread add_deffault_elements = new Thread(() -> {
            try {
                byte[] type = {(byte) 5};
                connect.sendPackages(type, null, null);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }

        });
        add_deffault_elements.start();
    }

    void remove_first(LinkedList<Potatoes> newclubni) {
        byte[] command = {(byte) 3};
        connect.sendPackages(command, null, null);
    }

    void save(LinkedList<Potatoes> newclubni, File file) {
        String filename = file.getPath();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
            output.write(gson.toJson(newclubni));
            output.close();
            new Audio(Main.class.getResourceAsStream("/save_saveAs.wav")).play();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    void getPotatoes() {
        byte[] command = {(byte) 6};
        connect.sendPackages(command, null, null);
    }

    Connect getConnect() {
        return connect;
    }
}