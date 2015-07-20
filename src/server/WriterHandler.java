///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package server;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.net.Socket;
//import java.util.concurrent.BlockingQueue;
//
//import util.IOHelper;
//import util.MessageTag;
//
///**
// *
// * @author datbt
// */
//public class WriterHandler implements Runnable{
//    private Socket connectionSocket;
//    private IOHelper ioHelper;
//    private int lineNum = 100;
//    private String NEWLINE = "\n";
//    private String TAB = "\t";
//    private int codeLength = 100;
//    private FileWriter fw;
//    private BufferedWriter bw;
//    private BlockingQueue<String> blockingQueue;
//    
//    public WriterHandler(Socket socket, BlockingQueue<String> queue){
//    	this.blockingQueue = queue;
//        this.connectionSocket = socket;
////        try {
////            this.ioHelper = null;// = new IOHelper();
////        } catch (IOException ex) {
////            //Logger.getLogger(WriterThread.class.getName()).log(Level.SEVERE, null, ex);
////        }
//    }
//    
//    @Override
//    public void run() {
//        //wait client send name
//        String msg;
//
//        msg = ioHelper.read();//client should return username tag + name
//        System.out.println(msg);
//        String tag = msg.substring(0, MessageTag.USERNAME.length());
//        String name = msg.substring(MessageTag.USERNAME.length());
//
//        if (tag.equals(MessageTag.USERNAME)){//if tag == username tag
//            int counter = 0;
//            
//            
//            try {
//            	if (blockingQueue == null){
//            		/*if using blocking queue, writing code to file will be taken by WriteToFile runnable
//            		 * this runnable's task is just read code from client and put to queue
//            		 * else we have to write code to file by ourself 
//            		 * so we have to initiate buffered writer to write code to file
//            		 * */
//	            	File file = new File("result/"+ name +".txt");
//	                if (!file.exists())
//	                    file.createNewFile();
//	
//	                fw = new FileWriter(file.getAbsoluteFile());
//	                bw = new BufferedWriter(fw);
//                }
//
//                //send ok msg to notice client to start send code
//                ioHelper.write(MessageTag.OK);
//
//                //start receive code from client and write to file
//                while (true){                    
//                    msg = ioHelper.read();
//                    //System.out.println("[MSG FROM CLIENT]\t" + msg);
//                    if (msg.length() == codeLength){//right code
//                    	if (blockingQueue == null){//we will write code to file if no blocking queue
//                    		bw.write(msg + NEWLINE);
//                    	}
//                    	else {//we just put code to queue and writing to file task is handle by other
//                    		blockingQueue.put(name + TAB + msg + NEWLINE); // we add a name prefix to easy to know this code come from which thread
//                    	}
//                        counter++;
//
//                        if (counter < lineNum){
//                            //request next code
//                            ioHelper.write(MessageTag.NEXT);
//                        }
//                        else{
//                            //enough code, send stop tag
//                            ioHelper.write(MessageTag.STOP);
//                            break;
//                        }
//                    }
//                    else { //code is wrong -- send error
//                        System.err.println("[Wrong code] wrong format code -- " + msg.length());
//                        ioHelper.write(MessageTag.ERROR);
//                        break;
//                    }
//                }              
//
//                //DONE
//
//            } catch (IOException | InterruptedException ex) {
//                //Logger.getLogger(WriterThread.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        
//        else {//error while authenticating -- send error tag and exit
//            ioHelper.write(MessageTag.ERROR);
//        }
//        
//        shutdown();        
//    }
//    
//    private void shutdown(){
//        try {
//            //close file buffered
//            if (bw != null)
//                bw.close();
//            if (fw != null)
//                fw.close();
//            
//            //close connection
//            //if (ioHelper != null)
//               // ioHelper.close();
//            if (connectionSocket != null)
//                connectionSocket.close();
//        } catch (IOException ex) {
//            System.err.println("Shutdown thread error!");
//        }
//    }
//    
//}
