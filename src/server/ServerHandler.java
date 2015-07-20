package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import util.IOHelper;
import util.MessageTag;
import util.User;

public class ServerHandler implements Runnable{
	private Selector selector;
	private ServerSocketChannel serverChannel;
	private IOHelper ioHelper;
	private int codeLength = 100;
	private int numOfCode = 5;
	
	public ServerHandler(ServerSocketChannel serverChannel, Selector selector){
		this.selector = selector;
		this.serverChannel = serverChannel;
		this.ioHelper = new IOHelper();
	}
	
	public ServerHandler(){
		ioHelper = new IOHelper();
		try {
			serverChannel = ServerSocketChannel.open();
			serverChannel.bind(new InetSocketAddress(34343));
	        serverChannel.configureBlocking(false);
	        selector = Selector.open(); 

	        serverChannel.register(selector, serverChannel.validOps());
	        
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}

	@Override
	public void run() {
		
		while (selector.isOpen()){
			System.out.println("wait for select");
			try {
				int count = selector.select();

				if (count == 0);
					continue;
			} catch (IOException e1) {
			}
			Set<SelectionKey> selectedKey = selector.selectedKeys();
			Iterator<SelectionKey> iter = selectedKey.iterator();
			//
			while (iter.hasNext()){
				System.err.println("run to here");
				SelectionKey key = iter.next();
				iter.remove();
				if (key.isAcceptable()){
					try {
						SocketChannel clientChannel = serverChannel.accept();
						clientChannel.configureBlocking(false);
						clientChannel.register(selector, SelectionKey.OP_READ);
						System.out.println("[server] new connection");
					} catch (IOException e) {
						System.err.println("Error while accepting new connection: " + e.getMessage());
					}
				}
				else if (key.isReadable()){
					System.out.println("[server] sending sth");
					try {
						handleChannel(key);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
				
			}
			
		}
		
	}
	
	private void handleChannel(SelectionKey key) throws IOException{
		SocketChannel clientChannel = (SocketChannel) key.channel();
		String msg = ioHelper.read(clientChannel);
		//check if msg is user name
		if (msg.startsWith(MessageTag.USERNAME)){//if user send name -- attach user to key and send OK to client
			System.out.println(msg);
			String name = msg.substring(MessageTag.USERNAME.length());
			User user;
			user = new User(name);
			if (user.isOK()){
				key.attach(user);
				ioHelper.write(MessageTag.OK, clientChannel);
			}
			else {				
				ioHelper.write(MessageTag.ERROR, clientChannel);
				clientChannel.close();
				user.closeWriter();
			}
			
		}
		else {//msg is code
			User user = (User)key.attachment();
			if (user == null || !user.isOK()){
				ioHelper.write(MessageTag.ERROR, clientChannel);
				clientChannel.close();
				if (user != null)
					user.closeWriter();
				return;
			}
			if (user.getNumOfCode() < numOfCode){//need more code
				if (msg.length() == codeLength){ //valid code -- write to file
					user.writeCodeToFile(msg);//write code to file
					ioHelper.write(MessageTag.NEXT, clientChannel);//send tag next to get next code
				}
				else {//enough code -- stop
					ioHelper.write(MessageTag.STOP, clientChannel);
					clientChannel.close();
					user.closeWriter();
				}
			}
		}
	}

}
