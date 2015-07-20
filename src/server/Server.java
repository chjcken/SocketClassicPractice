/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 *
 * @author datbt
 */
public class Server {

    private final ServerSocketChannel serverSocket;
    private final int port = 34343;
    private final Selector selector;
    //private final int threadNum = 2;

    public Server() throws IOException {
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        selector = Selector.open();           

    }

    public void start() throws IOException {
        System.out.println("Server start...");
        new Thread(new ServerHandler(serverSocket, selector)).start();
        
    }

    public static void main(String[] args) {
        new File("result").mkdir();
        Server server;
        try {
            server = new Server();
            server.start();
        } catch (IOException e) {
            System.err.println("Error while starting server" + e.getMessage());
        }

    }
}
