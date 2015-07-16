/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import util.IOHelper;

/**
 *
 * @author datbt
 */
public class Client {
    private static final String IPAddress = "localhost";
    private static final int port = 34343;
    
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(IPAddress, port));
            IOHelper ioHelper = new IOHelper(socketChannel);
            for (int i = 1; i<6; ++i)
                ioHelper.write("hello this is test message " + i + "\n");
            
        } catch (IOException ex) {
            System.err.println("error:" + ex.getMessage());
        }
    }
}
