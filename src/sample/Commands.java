package sample;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.LinkedList;

public class Commands {

    static private GsonBuilder gsonBuilder = new GsonBuilder();
    static private Gson gson = gsonBuilder.create();
    static private Type potatoesType = new TypeToken<Potatoes>() {
    }.getType();
    public static void add_if_max(String jsonStr, LinkedList<Potatoes> newclubni) {

        try {
            Potatoes pot = gson.fromJson(jsonStr, potatoesType);
            if (newclubni.size() != 0) {
                if (newclubni.element().getWeight() < pot.getWeight()) {
                    newclubni.addFirst(pot);
//                    new Audio(new File("sounds/add_if_max.wav")).play();
                }
            } else {
                newclubni.addFirst(pot);
//                new Audio(new File("sounds/add_if_max.wav")).play();
            }
        }  catch (Exception ignored) {}
    }
    public static void remove_greater(String jsonStr, LinkedList<Potatoes> newclubni) {
        try {
            Potatoes potatoes = gson.fromJson(jsonStr, potatoesType);
            newclubni.removeIf(potatoes1 -> potatoes.getWeight() < potatoes1.getWeight());
            new Audio(Main.class.getResourceAsStream("/remove_greater.wav")).play();
        } catch (Exception ignored) {        }
    }

    public static void remove_last(LinkedList<Potatoes> newclubni) {
        if (newclubni.size() != 0) {
            newclubni.removeLast();
//            new Audio(new File("sounds/remove_first-last.wav")).play();
        }
    }


    public static void add_default_elements(LinkedList<Potatoes> clubni) throws Exception {
        clubni.addLast(new Potatoes(3));
        clubni.addLast(new Potatoes(4));
        clubni.addLast(new Potatoes("Red", 8));
        clubni.addLast(new Potatoes("Red", 2));
        clubni.sort(
                (Potatoes pot1, Potatoes pot2) ->{
                       if (pot2.getWeight() - pot1.getWeight()>0){
                           return 1;
                       }
                       else if (pot2.getWeight() - pot1.getWeight()<0){
                           return -1;
                       }else {return 0;}
                });

    }
    public static void remove_first(LinkedList<Potatoes> newclubni) {
        if (newclubni.size() != 0) {
            newclubni.removeFirst();
//            new Audio(new File("sounds/remove_first-last.wav")).play();
        }
    }

    public static String save(LinkedList<Potatoes> newclubni, File file) {
        String filename = file.getPath();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
            output.write(gson.toJson(newclubni));
            output.close();
            new Audio(Main.class.getResourceAsStream("/save_saveAs.wav")).play();
            return "Файл сохранен";
        } catch (IOException ioe) {
            return ("Проблема с вводом данных");
        }
    }
}