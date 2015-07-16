/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.IOHelper;

/**
 *
 * @author datbt
 */
public class Server {
    private ServerSocketChannel serverSocket;
    private int port = 34343;
    
    public Server(){
        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(port));
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
    
    public void start(){
        System.out.println("Server start...");
        try {
            while(true){
                SocketChannel socketChanel = serverSocket.accept();
                IOHelper ioHelper = new IOHelper(socketChanel);
                String s = ioHelper.read();                
                System.out.println(s);
                
                s = ioHelper.read();                
                System.out.println(s);
                
                s = ioHelper.read();                
                System.out.println(s);
                
                s = ioHelper.read();                
                System.out.println(s);
                
                s = ioHelper.read();                
                System.out.println(s);
                
                s = ioHelper.read();                
                System.out.println(s);
            }
        } catch (IOException ex) {
            System.err.println("error:" + ex.getMessage());
        }        
    }
    
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
