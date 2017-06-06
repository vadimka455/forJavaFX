package sample;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by vadim on 01.06.2017.
 */
class Connect {
    private int port = 8888;
    private String host = "localhost";
    private SocketAddress socketAddress = new InetSocketAddress(host,port);
    private DatagramSocket datagramSocket;
    private LinkedList<Potatoes> listOfPotatoes = new LinkedList<>();
    private boolean canSetTree = false;
    {
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        getSendedPackage();

    }
    void testConnection(){
        if (!testConnect()){
            Stage badConnection = new Stage();
            System.out.println("Bad connection");
            //TODO (окошко "плохое подключение")
        }else {
            System.out.println("good connection");
        }

    }
    void sendPackages(byte[] command,byte[]object,byte[] hashObject){
        try {
//            testConnection();
            DatagramPacket datagramPacket = new DatagramPacket(command,command.length,socketAddress);
            datagramSocket.send(datagramPacket);
            System.out.println("send "+byteArrayToInt(command));
            if (object!=null){
//                byte[]hash=new byte[1000];
                datagramPacket=new DatagramPacket(object,object.length,socketAddress);
                datagramSocket.send(datagramPacket);
                System.out.println("Я тут отправил это " +byteArrayToInt(object));
//                datagramSocket.receive(new DatagramPacket(hash,hash.length,socketAddress));
                //TODO допилить сравнивание hash
            }
//            getSendedPackage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean testConnect(){
        try {
            byte[]fortest =new byte[3];
            byte[]test={1,2,3};
            DatagramPacket datagramPacket = new DatagramPacket(test,test.length,socketAddress);
            datagramSocket.send(datagramPacket);
            System.out.println(byteArrayToInt(test));
            datagramPacket= new DatagramPacket(fortest,fortest.length,socketAddress);
            datagramSocket.receive(datagramPacket);
            System.out.println(byteArrayToInt(fortest));
            //TODO тут что-то ломается со 2 раза
            //TODO дописать проверку (чтобы ожидал пару сек)
            return Arrays.equals(fortest, test);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    static byte[] intToByteArray(int value) {
        byte[] result = new byte[4];
        for(int i = 0; i < 4; i++) {
            result[i] = (byte)(value & 0xFF);
            value >>>= 8;
        }
        return result;
    }

    static int byteArrayToInt(byte[] bytes) {
        int result = 0;
        int l = bytes.length - 1;
        for(int i = 0; i < bytes.length; i++)
            if(i == l) result += bytes[i] << i * 8;
            else result += (bytes[i] & 0xFF) << i * 8;
        return result;
    }
    void getSendedPackage(){

        Thread sendedPackage= new Thread(()->{
            while (true) {
                LinkedList<Potatoes> list = new LinkedList<>();
                try {
                    byte[] sendedBack= new byte[1024];
                    DatagramPacket datagramPacket1 =  new DatagramPacket(sendedBack,sendedBack.length);
                    datagramSocket.receive(datagramPacket1);
                    System.out.println(byteArrayToInt(sendedBack));
                    int havingHashCode=0;
                    int neededHashCode=byteArrayToInt(sendedBack);
                    while (neededHashCode!=havingHashCode){
                        datagramSocket.receive(datagramPacket1);
                        Potatoes newPot= (Potatoes)new ObjectInputStream(new ByteArrayInputStream(sendedBack)).readObject();
                        list.add(newPot);
                        havingHashCode+=newPot.hashCode();
                    }
                    this.listOfPotatoes=list;
                    Platform.runLater(()->Main.setTreeView(listOfPotatoes));


                    /*for (byte b=sendedBack[0];b>0;b--){
                        datagramSocket.receive(datagramPacket1);
                        System.out.println("GetBack" +byteArrayToInt(sendedBack));
                        Potatoes newPot= (Potatoes)new ObjectInputStream(new ByteArrayInputStream(sendedBack)).readObject();
                        list.add(newPot);
                    }*/
                    //System.out.println(new String(sendedBack,"UTF-8"));
                    //LinkedList<Potatoes> objectInputStream =(LinkedList<Potatoes>)
                    // new ObjectInputStream(new ByteArrayInputStream(sendedBack)).readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        sendedPackage.start();
    }
    LinkedList<Potatoes> getListOfPotatoes(){
        return listOfPotatoes;
    }
    void setHost(String host){
        this.host=host;
    }
    boolean getCanSetTree(){
        return canSetTree;
    }
    void setCanSetTree(boolean set){
        this.canSetTree=set;
    }

}
