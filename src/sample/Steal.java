package sample;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.prefs.Preferences;

public class Steal {
    public LinkedList<Potatoes> clubni = new LinkedList<>();

    void stealPotatoes(File file) throws Exception {
        Thread loadObject = new Thread(() -> {
            String filename = file.getPath();
            read(filename);
        });
        loadObject.start();
        try {
            loadObject.join();
        }catch (InterruptedException e){e.printStackTrace();}
    }
    public void stealPotatoes() throws Exception {
        Thread loadObject = new Thread(() -> {
            Preferences prefs = Preferences.userNodeForPackage(Main.class);
            String filename = prefs.get("filePath", null);
            read(filename);

        });
        loadObject.start();
        try {
            loadObject.join();
        }catch (InterruptedException e){e.printStackTrace();}
    }
    private void read(String filename){
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            FileInputStream fileInputStream = new FileInputStream(filename);
            String json = new BufferedReader(new InputStreamReader(fileInputStream)).readLine();
            if (json == null || json.equals("")) {
                FileOutputStream fileOutputStream = new FileOutputStream(filename);
                OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
                output.write("[]");
                output.close();
                json = "[]";
            }
            Gson gson = gsonBuilder.create();
            Type potatoesListType = new TypeToken<LinkedList<Potatoes>>() {
            }.getType();
            Process.getSteal().clubni = gson.fromJson(json, potatoesListType);
            Process.getSteal().clubni.sort(
                    (Potatoes pot1, Potatoes pot2) ->
                            (int) (pot2.getWeight() - pot1.getWeight()));
        } catch (Exception ignored) {}
    }
}