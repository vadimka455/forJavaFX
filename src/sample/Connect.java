package sample;

import javafx.application.Platform;
import sample.Entity.Potatoes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.LinkedList;

/**
 * Created by vadim on 01.06.2017.
 */
class Connect {
    private int port = 8888;
    private String host = "192.168.43.170";
    private SocketAddress socketAddress = new InetSocketAddress(host, port);
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

    static byte[] intToByteArray(int value) {
        byte[] result = new byte[4];
        for (int i = 0; i < 4; i++) {
            result[i] = (byte) (value & 0xFF);
            value >>>= 8;
        }
        return result;
    }

    static int byteArrayToInt(byte[] bytes) {
        int result = 0;
        int l = bytes.length - 1;
        for (int i = 0; i < bytes.length; i++)
            if (i == l) result += bytes[i] << i * 8;
            else result += (bytes[i] & 0xFF) << i * 8;
        return result;
    }

    void sendPackages(byte[] command, byte[] object, byte[] hashObject) {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(command, command.length, socketAddress);
            datagramSocket.send(datagramPacket);
            if (object != null) {
                datagramPacket = new DatagramPacket(object, object.length, socketAddress);
                datagramSocket.send(datagramPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void getSendedPackage() {

        Thread sendedPackage = new Thread(() -> {
            while (true) {
                LinkedList<Potatoes> list = new LinkedList<>();
                try {
                    byte[] sendedBack = new byte[1024];
                    DatagramPacket datagramPacket1 = new DatagramPacket(sendedBack, sendedBack.length);
                    datagramSocket.receive(datagramPacket1);
                    int havingHashCode = 0;
                    int neededHashCode = byteArrayToInt(sendedBack);
                    while (neededHashCode != havingHashCode) {
                        datagramSocket.receive(datagramPacket1);
                        Potatoes newPot = (Potatoes) new ObjectInputStream(new ByteArrayInputStream(sendedBack)).readObject();
                        System.out.println("Recieve Potatoes #"+newPot.getId());
                        list.add(newPot);
                        havingHashCode += newPot.hashCode();
                    }
                    this.listOfPotatoes = list;
                    Platform.runLater(() -> Main.setTreeView(listOfPotatoes));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        sendedPackage.start();
    }

    LinkedList<Potatoes> getListOfPotatoes() {
        return listOfPotatoes;
    }

    boolean getCanSetTree() {
        return canSetTree;
    }

}
