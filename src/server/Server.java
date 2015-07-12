/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *
 * @author datbt
 */
public class Server {
    private static final int port = 34343;
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool;
    private final int nThread = 16;
    private BlockingQueue<String> blockingQueue;
    
    public static int OLD_STYLE = 0;
    public static int THREAD_POOL_STYLE = 1;
    
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(port);
        threadPool = Executors.newFixedThreadPool(nThread);
        blockingQueue = null;
    }
    
    private void startOldMultiThreadStyle(){
        while (true){         
            try {
                new Thread(new WriterHandler(serverSocket.accept(), blockingQueue)).start();
                //System.out.println("Start new thread...");
            } catch (IOException ex) {
                //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            	System.err.println("Error while connecting with client!");
            }
        }
    }
    
    private void startThreadPoolMultiThreadStyle(){
        try {
            while (true){
                threadPool.execute(new WriterHandler(this.serverSocket.accept(), blockingQueue));
            }
        } catch (IOException ex) {
            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        	System.err.println("Error while connecting with client!");
        }
        
    }
    
    public void start(int serverType){        
        System.out.println((serverType == 0 ?"Old style":"Thread pool style") + " server is running...");
        if (serverType == Server.OLD_STYLE){
            startOldMultiThreadStyle();
        }
        else {
            startThreadPoolMultiThreadStyle();
        }        
    }
    
    public void startWithBlockingQueue(int serverType){
    	System.out.println("Blocking queue mode enable.");
    	System.out.println((serverType == 0 ?"Old style":"Thread pool style") + " server is running...");
    	//create new blocking queue
    	this.blockingQueue = new ArrayBlockingQueue<>(1000, false);
    	
    	//start thread to take code from queue and write to file
    	new Thread(new WriteToFileHandler(blockingQueue)).start();
    	
    	//start service
    	if (serverType == Server.OLD_STYLE){
            startOldMultiThreadStyle();
        }
        else {
            startThreadPoolMultiThreadStyle();
        }   
    	
    }
    
    public static void main(String[] args) throws IOException {
    	//create folder to contain written file
    	new File("result").mkdir();
        int serverType = Server.OLD_STYLE;
        try {
        	serverType = args.length !=0? Integer.parseInt(args[0]) : Server.THREAD_POOL_STYLE;
        }
        catch (Exception e){
        	System.err.println("Wrong format parameter! Default old style multi thread server will be started.");
        }
        try {
			Server server = new Server();
			server.start(serverType);
			//server.startWithBlockingQueue(serverType);
		} catch (IOException e) {
			System.err.println("Error while starting server! Maybe port has already been used!");
		}
    }
}
