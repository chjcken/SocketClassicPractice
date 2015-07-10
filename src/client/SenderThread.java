package client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import socketio.IOHelper;
import socketio.MessageTag;

public class SenderThread extends Thread {
	private String TAG = "[SenderThread]\t";
	private Socket clientSocket;
	private String code;
	private String name;
	private IOHelper ioHelper;
	private long executingTime = 0;
		
	
	public SenderThread(Socket socket, String name, String code){
		this.clientSocket = socket;
		this.code = code;
		this.name = name;
		try {
			this.ioHelper = new IOHelper(clientSocket.getInputStream(), clientSocket.getOutputStream());
                        //System.out.println("new ioHelper done!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//counting time executing
		long now = System.currentTimeMillis();
		//send username to server
		ioHelper.write(MessageTag.USERNAME+this.name);
            
		String msg = "";
		boolean done = false;
                
		
		//get ok tag from server
		msg = ioHelper.read();
                System.err.println("[WAIT OK MSG FROM SERVER]\t" + msg);
		if (msg.equals(MessageTag.OK)){//server ready -- start sending code                    
			while (!done){
				//send code
                            //System.out.println("[code to send]\t" + code);
				ioHelper.write(code);
				//then wait server answer
				msg = ioHelper.read();
                                //System.err.println("[MSG FROM SERVER]\t" + msg);
				if (msg.equals(MessageTag.NEXT)){
                                    //continue send code

				}
				else if (msg.equals(MessageTag.STOP)){
						//enough code -- stop
                                    System.err.println("Server say Stop");
						done = true;
					}
				else {
					//something wrong                                    
					System.out.println(TAG + "error while sending code");
                                        break;
				}
				
			}			
		}
		//TAG != OK --- error -- server doesnt accept sending 
		else {
			System.out.println(TAG + "error: server denied");
		}
		
		//DONE! 
		this.executingTime = System.currentTimeMillis() - now;
		
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public long getExecutingTime(){
		return this.executingTime;
	}

	
}
