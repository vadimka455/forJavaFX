package sample;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.LinkedList;

class Commands {

    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson = gsonBuilder.create();

    private Type potatoesType = new TypeToken<Potatoes>() {}.getType();
    private Connect connect= new Connect();
    void add_if_max(String jsonStr, LinkedList<Potatoes> newclubni) {

        try {
            Potatoes pot = gson.fromJson(jsonStr, potatoesType);

            Thread add_if_max= new Thread(()->{
                try {
                    System.out.println(jsonStr);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024*1024);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(pot);
                    byte[]type ={(byte)1};
                    connect.sendPackages(type,byteArrayOutputStream.toByteArray(),Connect.intToByteArray(pot.hashCode()));
                    //byte [] data= ArrayUtils.addAll(type,byteArrayOutputStream.toByteArray());
                    byteArrayOutputStream.close();
                    System.out.println(new String(type));
//                    new Connect().setGetBack(true);
//                    new Connect().getSendedPackge();

                    System.out.println("Отправил 1");
                }catch (Exception ignored){
                    ignored.printStackTrace();
                }

            });
            add_if_max.start();


        }  catch (Exception ignored) {}
    }
    void remove_greater(String jsonStr, LinkedList<Potatoes> newclubni) {
        Potatoes pot =new Potatoes();
        Thread remove_greater= new Thread(()->{
            try {
                System.out.println(jsonStr);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024*1024);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(pot);
                byte[]type ={(byte)2};
                connect.sendPackages(type,byteArrayOutputStream.toByteArray(),Connect.intToByteArray(pot.hashCode()));
                //byte [] data= ArrayUtils.addAll(type,byteArrayOutputStream.toByteArray());
                byteArrayOutputStream.close();
                System.out.println(new String(type));
//                new Connect().setGetBack(true);
//                new Connect().getSendedPackge();
                //System.out.println(new String(data));

                System.out.println("Отправил 2");
            }catch (Exception ignored){
                ignored.printStackTrace();
            }

        });
        remove_greater.start();
    }

    void remove_last(LinkedList<Potatoes> newclubni) {
        byte[]command={(byte)4};
        connect.sendPackages(command,null,null);
//        new Connect().setGetBack(true);
//        new Connect().getSendedPackge();
    }


    void add_default_elements(LinkedList<Potatoes> clubni) throws Exception {

        Thread add_deffault_elements= new Thread(()->{
            try {
                byte[]type ={(byte)5};
                connect.sendPackages(type,null, null);
                System.out.println("Можно? " +connect.getCanSetTree());
                //byte [] data= ArrayUtils.addAll(type,byteArrayOutputStream.toByteArray());

//                new Connect().setGetBack(true);
//                new Connect().getSendedPackge();
                //System.out.println(new String(data));

                System.out.println("Отправил 5");
//                new Connect().getSendedPackge();
            }catch (Exception ignored){
                ignored.printStackTrace();
            }

        });
        add_deffault_elements.start();
    }
   void remove_first(LinkedList<Potatoes> newclubni) {
       byte[]command={(byte)3};
       connect.sendPackages(command,null,null);
//       new Connect().setGetBack(true);
       //new Connect().getSendedPackge();
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
        }
    }
    void getPotatoes(){
        byte[]command={(byte)6};
        connect.sendPackages(command,null,null);
    }
    Connect getConnect(){
        return connect;
    }
}