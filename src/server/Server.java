/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

//import java.io.PrintWriter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import socketio.IOHelper;
import socketio.MessageTag;


/**
 *
 * @author datbt
 */
public class Server {
    private static final int port = 34343;
    public Server() {
        
    }
    
    public static void main(String[] args) throws IOException {
    	boolean isRunning = true;
        ServerSocket serverSocket;
        serverSocket = new ServerSocket(port);
        System.out.println("Server is running...");
        while (isRunning){
            Socket connectionSocket = serverSocket.accept();
            IOHelper ioHelper = new IOHelper(connectionSocket.getInputStream(),
                                                connectionSocket.getOutputStream());
            //say hello to client
            ioHelper.write(MessageTag.HELLO);
            
            String msg = "";
            
            msg = ioHelper.read();
            System.out.println(msg);
            String tag = msg.substring(0, MessageTag.USERNAME.length());
            String name = msg.substring(MessageTag.USERNAME.length());
            
            if (tag.equals(MessageTag.USERNAME)){//username tag
            	//msg = ioHelper.read();//read username -- this will be file name
                //TODO start thread to handle this user
                //WriterThread writerThread = 
                    new WriterThread(connectionSocket, name).start(); 
                    System.out.println("Start new thread...");
            }
            else {//not username tag -> send error tag
                System.err.println("[WRONG TAG]\t wrong username tag");
                ioHelper.write(MessageTag.ERROR);
            }
        }
        
        serverSocket.close();
        
        //WriterThread.testWrite();
    }
}
