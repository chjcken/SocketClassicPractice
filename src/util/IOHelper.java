/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *
 * @author datbt
 */
public class IOHelper {
    private final String TAG = "[IOHelper]\t";
    //private final SocketChannel socketChanel;
    public IOHelper(){
        //this.socketChanel = socketChannel;
       
    }
    
    public void write(String msg, SocketChannel socketChannel){
        try {
            ByteBuffer buf = ByteBuffer.wrap(msg.getBytes());//ByteBuffer.allocate(msg.length());
            //buf.clear();
            //buf.put(msg.getBytes());
            //buf.flip();
            //while (buf.hasRemaining()){ 
                socketChannel.write(buf);
             //}
            buf.clear();
            
        } catch (IOException|NullPointerException ex) {
            System.err.println(TAG + "error in write(): " + ex.getMessage());
        }
        
    }
    
    public String read(SocketChannel socketChannel){
        String result = "";
        ByteBuffer buf = ByteBuffer.allocate(128);
        try {            
        	socketChannel.read(buf);        
            result = new String(buf.array()).trim();     
        } catch (IOException ex) {
            result = "";
            System.err.println(TAG + "error in read(): " + ex.getMessage());
        }
        return result;
    }
}
