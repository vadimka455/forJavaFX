package sample.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
class ServerSendingPackage {
    private static int port = 8888;
    private static String host = "0.0.0.0";
    private static SocketAddress socketAddress = new InetSocketAddress(host,port);
    private static DatagramChannel datagramChannel;

    DatagramChannel start () throws IOException {
        datagramChannel = DatagramChannel.open();
        datagramChannel.bind(socketAddress);
        return datagramChannel;
    }

}
