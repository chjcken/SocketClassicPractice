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
    private ServerSocketChannel serverSocket;
    private int port = 34343;
    private Selector selector;
    
    public Server() throws IOException{        
//        serverSocket = ServerSocketChannel.open();
//        serverSocket.bind(new InetSocketAddress(port));
//        serverSocket.configureBlocking(false);
//        selector = Selector.open();           
        
    }
    
    public void start(){
        System.out.println("Server start...");
//        try {
//			SelectionKey key = serverSocket.register(selector, serverSocket.validOps());
//		} catch (ClosedChannelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}     
        new Thread(new ServerHandler()).start();
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
