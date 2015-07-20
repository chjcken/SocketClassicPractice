package client;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import util.IOHelper;
import util.MessageTag;


public class SenderThread extends Thread {
	private String TAG = "[SenderThread]\t";
    private SocketChannel clientSocket;
    private String code;
    private String name;
    private IOHelper ioHelper;
    private long executingTime = 0;
    
    public SenderThread(SocketChannel socket, String name, String code) {
		this.clientSocket = socket;
		this.name = name;
		this.code = code;
		this.ioHelper = new IOHelper();		
		
	}
    
    @Override
    public void run() {
    	//counting time executing
    	try {
			clientSocket.finishConnect();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        long now = System.currentTimeMillis();
        
        String msg;
        
        //send username to server
        ioHelper.write(MessageTag.USERNAME+this.name, clientSocket);

        boolean done = false;
       

        //get ok tag from server
        msg = ioHelper.read(clientSocket); System.out.println("can we run to here?");
        //System.err.println("[WAIT OK MSG FROM SERVER]\t" + msg);
        if (msg.equals(MessageTag.OK)){//server ready -- start sending code                    
            while (!done){
                //send code
                ioHelper.write(code, clientSocket);
                //then wait server answer
                msg = ioHelper.read(clientSocket);
                //System.err.println("[MSG FROM SERVER]\t" + msg);
                if (msg.equals(MessageTag.NEXT)){
                    //continue send code

                }
                else if (msg.equals(MessageTag.STOP)){
                    //enough code -- stop
                    //System.out.println("Server say Stop");
                    done = true;
                }
                else {
                        //something wrong                                    
                        System.err.println(TAG + "error while sending code in thread: " + name);
                        this.executingTime = -1;
                        break;
                }
            }
            
        //DONE! 
        if (this.executingTime != -1)
        	this.executingTime = System.currentTimeMillis() - now;
        }
        
        else { //TAG != OK --- error -- server doesnt accept sending 
            System.err.println(TAG + "error: server denied");
            this.executingTime = -1;
        }
        
        try {
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
    }

	public long getExecutingTime() {
		return executingTime;
	}
    
    
	
}
