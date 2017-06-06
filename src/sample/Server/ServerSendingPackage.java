package sample.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Created by vadim on 05.06.2017.
 */
public class ServerSendingPackage {
    private static int port = 8888;
    private static String host = "0.0.0.0";
    private static SocketAddress socketAddress = new InetSocketAddress(host,port);
    private static DatagramChannel datagramChannel;
    private static ServerProcess serverProcess=new ServerProcess();
    private static ByteBuffer byteBuffer = ByteBuffer.allocate(1024*1024*1024);

    DatagramChannel start () throws IOException {
        datagramChannel = DatagramChannel.open();
        datagramChannel.bind(socketAddress);
        return datagramChannel;
    }

}
