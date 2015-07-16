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
    private final SocketChannel socketChanel;
    public IOHelper(SocketChannel socketChannel){
        this.socketChanel = socketChannel;
       
    }
    
    public void write(String msg){
        try {
            ByteBuffer buf = ByteBuffer.wrap(msg.getBytes());//ByteBuffer.allocate(msg.length());
            //buf.clear();
            //buf.put(msg.getBytes());
            //buf.flip();
            while (buf.hasRemaining()){ 
                socketChanel.write(buf);
             }
        } catch (IOException|NullPointerException ex) {
            System.err.println(TAG + "error in write(): " + ex.getMessage());
        }
        
    }
    
    public String read(){
        String result = "";
        
        ByteBuffer buf = ByteBuffer.allocate(128);
        try {            
            int nByte = socketChanel.read(buf);
            if (nByte != -1){
                //result = decoder.decode(buf).toString();
                result = new String (buf.array());                
            }      
            
        } catch (IOException ex) {
            result = "";
            System.err.println(TAG + "error in read(): " + ex.getMessage());
        }
        return result;
    }
}
