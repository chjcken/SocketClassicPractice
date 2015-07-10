/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author datbt
 */
public class Server {
    private static final int port = 34343;
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool;
    private final int nThread = 10;
    
    public static int OLD_STYLE = 0;
    public static int THREAD_POOL_STYLE = 1;
    
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(port);
        threadPool = Executors.newFixedThreadPool(nThread);        
    }
    
    private void startOldMultiThreadStyle(){
        while (true){         
            try {
                new Thread(new WriterHandler(serverSocket.accept())).start();
                System.out.println("Start new thread...");
            } catch (IOException ex) {
                //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void startThreadPoolMultiThreadStyle(){
        try {
            while (true){
                threadPool.execute(new WriterHandler(this.serverSocket.accept()));
            }
        } catch (IOException ex) {
            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void start(int serverType){        
        System.out.println("Server is running...");
        if (serverType == Server.OLD_STYLE){
            startOldMultiThreadStyle();
        }
        else {
            startThreadPoolMultiThreadStyle();
        }
        
    }
    
    public static void main(String[] args) throws IOException {
//    	boolean isRunning = true;
//        ServerSocket serverSocket;
//        serverSocket = new ServerSocket(port);
//        System.out.println("Server is running...");
//        while (isRunning){         
//            new Thread(new WriterHandler(serverSocket.accept())).start();
//            System.out.println("Start new thread...");
//        }
//        
//        serverSocket.close();
        
        Server server = new Server();
        server.start(Server.THREAD_POOL_STYLE);
    }
}
