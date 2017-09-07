package sample.Server;

import org.postgresql.util.PSQLException;
import sample.Entity.Potatoes;
import sample.Entity.Vegetables;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;

public class Server {
    private static ServerSendingPackage sendingPackage = new ServerSendingPackage();
    private static ServerDB serverDB = new ServerDB();
    private static int port = 8888;
    private static String host = "0.0.0.0";
    private static SocketAddress socketAddress = new InetSocketAddress(host, port);
    private static SocketAddress remoteAddress;
    private static HashSet<SocketAddress> remoteAddresses = new HashSet<>();
    private static DatagramChannel datagramChannel;
    private static ServerProcess serverProcess = new ServerProcess();
    private static ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private static boolean sendBack = false;

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome");
        datagramChannel = sendingPackage.start();

        Connection connection = serverDB.connect();
        serverDB.initTable(connection,Vegetables.class);

        while (true) {
            byteBuffer.clear();
            remoteAddress = datagramChannel.receive(byteBuffer);
            remoteAddresses.add(remoteAddress);
            byteBuffer.flip();
            int limits = byteBuffer.limit();
            byte[] forCommand = new byte[limits];
            byte[] forObject = new byte[3];
            byte[] fortest = {1, 2, 3};

            byteBuffer.get(forCommand, 0, limits);

            /*test*/
            if (Arrays.equals(forCommand, fortest)) {
                byteBuffer.clear();
                byteBuffer.put(forCommand);
                byteBuffer.flip();
                datagramChannel.send(byteBuffer, remoteAddress);
                byteBuffer.clear();
                continue;
            }
            if (forCommand[0] == (byte) 1 || forCommand[0] == (byte) 2) {
                byteBuffer.clear();
                datagramChannel.receive(byteBuffer);
                byteBuffer.flip();
                limits = byteBuffer.limit();
                forObject = new byte[limits];
                byteBuffer.get(forObject, 0, limits);
            }
            byteBuffer.clear();
            switch (forCommand[0]) {
                case (byte) 1: {
                    byte[] forObject2 = forObject;
                    new Thread(() -> {
                        new ServerCommands().add_if_max(forObject2);
                        sendPackageBack();
                    }).start();
                    break;
                }
                case (byte) 2: {
                    byte[] forObject2 = forObject;
                    new Thread(() -> {
                        new ServerCommands().remove_greater(forObject2);
                        sendPackageBack();
                    }).start();
                    break;
                }
                case (byte) 3: {
                    new Thread(() -> {
                        new ServerCommands().remove_first();
                        sendPackageBack();
                    }).start();
                    break;
                }
                case (byte) 4: {
                    new Thread(() -> {
                        new ServerCommands().remove_last();
                        sendPackageBack();
                    }).start();
                    break;
                }
                case (byte) 5: {
                    new Thread(() -> {
                        new ServerCommands().add_default_elements();
                        sendPackageBack();
                    }).start();
                    break;
                }
                case (byte) 6: {
                    new Thread(() -> {
                        sendPackageBack();
                    }).start();
                    break;
                }
            }
        }
    }

    private static byte[] intToByteArray(int value) {
        byte[] result = new byte[4];
        for (int i = 0; i < 4; i++) {
            result[i] = (byte) (value & 0xFF);
            value >>>= 8;
        }
        return result;
    }

    private static int byteArrayToInt(byte[] bytes) {
        int result = 0;
        int l = bytes.length - 1;
        for (int i = 0; i < bytes.length; i++)
            if (i == l) result += bytes[i] << i * 8;
            else result += (bytes[i] & 0xFF) << i * 8;
        return result;
    }

    static boolean getSendBack() {
        return sendBack;
    }

    static void setSendBack(boolean send) {
        sendBack = send;
    }

    private static void sendPackageBack() {


        Thread sendBackThread = new Thread(() -> {
            for (SocketAddress rm : remoteAddresses) {
                try {
                    byteBuffer.clear();
//                    byte[] length ={(byte) newclubni.size()};
//                    byteBuffer.put(length);
                     LinkedList<Potatoes> listOfPotatoes = serverDB.getData();
                    int hashCode = 0;
                    for (Potatoes potatoes : listOfPotatoes) {
                        hashCode += potatoes.hashCode();
                    }
                    byteBuffer.put(intToByteArray(hashCode));
                    byteBuffer.flip();
                    datagramChannel.send(byteBuffer, rm);
                    byteBuffer.flip();
                    byteBuffer.clear();
                    for (Potatoes potatoes : listOfPotatoes) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        objectOutputStream.writeObject(potatoes);
                        byteBuffer.put(byteArrayOutputStream.toByteArray());
                        byteBuffer.flip();
                        datagramChannel.send(byteBuffer, rm);
                        System.out.println("Potatoe #" + potatoes.getId() + " send");
                        byteBuffer.clear();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        sendBackThread.start();

    }
}
