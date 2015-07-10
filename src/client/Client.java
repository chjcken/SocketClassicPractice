/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import socketio.MessageTag;

/**
 *
 * @author datbt
 */
public class Client {

	private static final String IPAddress = "localhost";
    private static final int port = 34343;
    private static final int threadNum = 100;
    
    public void _wait(long milisecond){
        long now = System.currentTimeMillis();
        while (true){
            if (System.currentTimeMillis() - now > milisecond)
                break;
        }
    }
    
    public static void main(String[] args)  {
    	ArrayList<SenderThread> listThread = new ArrayList<>();
    	long now = System.currentTimeMillis();
        for (int i = 0; i<threadNum; ++i){
	        try {
	            
                    SenderThread senderThread = new SenderThread(new Socket(IPAddress, port), 
                                                String.valueOf(i), MessageTag.generateCode(100));
                    senderThread.start();
                    listThread.add(senderThread);                   
                    
	            
	        } catch (IOException ex) {
	            System.err.println("IO exception");
	        }    	
        }
        
        //wait for all threads done their job
        for (SenderThread thread : listThread){
            try {
                    thread.join();
            } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
        }
        
        //calculate time executing
        long realExecutingTime = System.currentTimeMillis() - now;
        long totalTime = 0;
        int nThreadSuccess = 0;
        for (SenderThread thread : listThread){
            long time = thread.getExecutingTime();
            if (time > -1){ // time == -1 means error
            	totalTime += time;
                nThreadSuccess++;
            }
        }
        long averageExecutingTime = nThreadSuccess != 0? totalTime/nThreadSuccess : 0;
        
        System.out.println("Number of threads send code successfully: " + nThreadSuccess + "/" + threadNum);
        System.out.println("Average time executing per thread = " + averageExecutingTime + "ms");
        System.out.println("Total time executing " + threadNum + " threads = " + realExecutingTime + "ms");
        
    }
}
