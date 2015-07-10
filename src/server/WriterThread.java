package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import socketio.IOHelper;
import socketio.MessageTag;

public class WriterThread extends Thread {
	private Socket connectionSocket;
	private IOHelper ioHelper;
	private String fileName;
        private int lineNum = 100;
        private String NEWLINE = "\n";
        private int codeLength = 100;
	
	public WriterThread(Socket socket, String name) {
		this.connectionSocket = socket;
		this.fileName = name + ".txt";
            try {
                this.ioHelper = new IOHelper(connectionSocket.getInputStream(),
                        connectionSocket.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(WriterThread.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
	
	@Override
	public void run() {
            //prepare file for writing
            int counter = 0;
            File file = new File("result/"+fileName);
            FileWriter fw;
            BufferedWriter bw;
            String msg = "";
            try {
                if (!file.exists())
                    file.createNewFile();
                
                fw = new FileWriter(file.getAbsoluteFile());
                bw = new BufferedWriter(fw);
                    
                //send ok msg to notice client to start send code
                ioHelper.write(MessageTag.OK);
                
                //start receive code from client and write to file
                while (true){                    
                    msg = ioHelper.read();
                    //System.out.println("[MSG FROM CLIENT]\t" + msg);
                    if (msg.length() == codeLength){//right code
                        bw.write(msg + NEWLINE);
                        counter++;

                        if (counter < lineNum){
                            //request next code
                            ioHelper.write(MessageTag.NEXT);
                        }
                        else{
                            //enough code, send stop tag
                            ioHelper.write(MessageTag.STOP);
                            break;
                        }
                    }
                    else { //code is wrong -- send error
                        System.err.println("[Wrong code] wrong format code -- " + msg.length());
                        ioHelper.write(MessageTag.ERROR);
                        break;
                    }
                }
                
                //close file buffered
                bw.close();
                fw.close();
                
                //close connection
                connectionSocket.close();
                
                //DONE
                
            } catch (IOException ex) {
                Logger.getLogger(WriterThread.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
        
//        public static void testWrite(){
//            String NEWLINE = "\n";
//            File file = new File("result/test.txt");
//            try {
//                if (!file.exists())                
//                    file.createNewFile();
//                FileWriter fw = new FileWriter(file.getAbsoluteFile());
//                BufferedWriter bw = new BufferedWriter(fw);
//                bw.write("test write file java" + NEWLINE);
//                bw.write("write more...");
//                
//                bw.close();
//                fw.close();
//                
//            } catch (IOException ex) {
//                Logger.getLogger(WriterThread.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
}
