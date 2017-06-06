package sample.Server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sample.Potatoes;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.LinkedList;

class ServerCommands {
    private int port = 8888;
    private String host = "localhost";
    private SocketAddress socketAddress = new InetSocketAddress(host,port);
    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson = gsonBuilder.create();
    private ServerDB serverDB = new ServerDB();
    private Potatoes readPotatoes(byte[] object){
        Potatoes potatoes = new Potatoes();
        System.out.println("ПОлучил объект");
        try {
            java.lang.Object objectInputStream =new ObjectInputStream(new ByteArrayInputStream(object)).readObject();
            return (Potatoes) objectInputStream;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return potatoes;
    }
    void add_if_max(byte[] object) {
        Potatoes pot=readPotatoes(object);
        LinkedList<Potatoes> newclubni = serverDB.getData();
        LinkedList<Potatoes> element = new LinkedList<>();
        try {
            if (newclubni.size() != 0) {
                if (newclubni.element().getWeight() < pot.getWeight()) {
                    element.add(pot);
                    serverDB.putData(element);
//                    new Audio(new File("sounds/add_if_max.wav")).play();
                }
            } else {
                element.addFirst(pot);
                serverDB.putData(element);
//                new Audio(new File("sounds/add_if_max.wav")).play();
            }
        }  catch (Exception ignored) {
            ignored.printStackTrace();
        }
        System.out.println(new Gson().toJson(newclubni));
        System.out.println("add_if_max");
    }
    void remove_greater( byte[] object) {
        Potatoes potatoes =readPotatoes(object);
        LinkedList<Potatoes> needToDelete = serverDB.getData();
        System.out.println(potatoes.getWeight());

        try {
            needToDelete.removeIf(potatoes1 -> potatoes.getWeight() >= potatoes1.getWeight());
            serverDB.deleteData(needToDelete);
            //new Audio(Main.class.getResourceAsStream("/remove_greater.wav")).play();
        } catch (Exception ignored) {        }
        System.out.println(new Gson().toJson(needToDelete));
        System.out.println("remove_greater");
    }

    void remove_last() {
        LinkedList<Potatoes> newclubni = serverDB.getData();
        if (newclubni.size() != 0) {
            LinkedList<Potatoes> needToDelete= new LinkedList<>();
            needToDelete.add(newclubni.getLast());
            serverDB.deleteData(needToDelete);
//            new Audio(new File("sounds/remove_first-last.wav")).play();
        }
        System.out.println("remove_last");
    }


    void add_default_elements() {
        LinkedList<Potatoes> needToAdd =new LinkedList<>();
        needToAdd.addLast(new Potatoes(3));
        needToAdd.addLast(new Potatoes(4));
        needToAdd.addLast(new Potatoes("Red", 8));
        needToAdd.addLast(new Potatoes("Red", 2));
        serverDB.putData(needToAdd);

    }
    void remove_first() {
        LinkedList<Potatoes> newclubni=serverDB.getData();
        if (newclubni.size() != 0) {
            LinkedList<Potatoes> needToDelete = new LinkedList<>();
            needToDelete.add(newclubni.getFirst());
            serverDB.deleteData(needToDelete);
//            new Audio(new File("sounds/remove_first-last.wav")).play();

        }
        System.out.println("remove_first");
    }

    void save(LinkedList<Potatoes> newclubni, File file) {
        String filename = file.getPath();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
            output.write(gson.toJson(newclubni));
            output.close();
            //new Audio(Main.class.getResourceAsStream("/save_saveAs.wav")).play();
        } catch (IOException ignored) {
        }
    }

}