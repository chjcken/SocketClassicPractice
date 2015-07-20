/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import util.IOHelper;
import util.MessageTag;

/**
 *
 * @author datbt
 */
public class Client {
    private static final String IPAddress = "localhost";
    private static final int port = 34343;
    private static final int threadNum = 1;
    
    public static void main(String[] args) {
    	ArrayList<SenderThread> listThread = new ArrayList<>();
    	long now = System.currentTimeMillis();
    	InetSocketAddress address = new InetSocketAddress(IPAddress, port);
    	try {
			SocketChannel socketChannel = SocketChannel.open();	            
			socketChannel.connect(address);
			IOHelper io = new IOHelper();
			io.write("USERNAM0", socketChannel);
			System.out.println(io.read(socketChannel));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	for (int i = 0; i < threadNum; ++i){
//	        try {
//	            SocketChannel socketChannel = SocketChannel.open();	            
//	            socketChannel.connect(address);
//	            
//	            SenderThread thread = new SenderThread(socketChannel, String.valueOf(i),
//	            										MessageTag.generateCode(100));
//	            thread.start();
//	            listThread.add(thread);
//	            
//	            
//	        } catch (IOException ex) {
//	            System.err.println("error:" + ex.getMessage());
//	        }
//    	}
//    	
//    	for (SenderThread thread : listThread){
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                    
//            }
//        }
//        
//        //calculate time executing
//        long realExecutingTime = System.currentTimeMillis() - now;
//        long totalTime = 0;
//        int nThreadSuccess = 0;
//        for (SenderThread thread : listThread){
//            long time = thread.getExecutingTime();
//            if (time > -1){ // time == -1 means error
//            	totalTime += time;
//                nThreadSuccess++;
//            }
//        }
//        long averageExecutingTime = nThreadSuccess != 0? totalTime/nThreadSuccess : 0;
//        
//        System.out.println("Number of threads send code successfully: " + nThreadSuccess + "/" + threadNum);
//        System.out.println("Average time executing per thread = " + averageExecutingTime + "ms");
//        System.out.println("Total time executing " + threadNum + " threads = " + realExecutingTime + "ms");
    }
}
