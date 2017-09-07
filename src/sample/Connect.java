package sample;

import javafx.application.Platform;
import sample.Entity.Potatoes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.LinkedList;

import static sample.IntByteOperation.byteArrayToInt;


class Connect extends AConnect{
    private int port = 1488;
    private String host = "localhost";
    private SocketAddress socketAddress;
    private DatagramSocket datagramSocket;
    private LinkedList<Potatoes> listOfPotatoes;

    public Connect(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public Connect(String host) {
        this.host = host;
    }

    public Connect() {
        this.port = 1488;
        this.host = "localhost";
    }

    {
        try {
            socketAddress = new InetSocketAddress(host,port);
            datagramSocket = new DatagramSocket();
            listOfPotatoes = new LinkedList<>();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        getSendedPackage();

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

    //TODO привести в порядок
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
                    if (!listOfPotatoes.isEmpty()) {
                        Platform.runLater(() -> Main.setTreeView(listOfPotatoes));
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        sendedPackage.start();
    }
    public void mac(){

    }

    LinkedList<Potatoes> getListOfPotatoes() {
        return listOfPotatoes;
    }

}
