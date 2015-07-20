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
    
    public void write(String msg, SocketChannel socketChannel){
        try {
            ByteBuffer writeBuf = ByteBuffer.wrap(msg.getBytes());
            socketChannel.write(writeBuf);
            writeBuf.clear();
            
        } catch (IOException|NullPointerException ex) {
            System.err.println(TAG + "error in write(): " + ex.getMessage());
        }
        
    }
    
    public String read(SocketChannel socketChannel){
        String result;
        ByteBuffer readBuf = ByteBuffer.allocate(128);
        try {            
            socketChannel.read(readBuf);        
            result = new String(readBuf.array()).trim();     
        } catch (IOException ex) {
            result = "";
            System.err.println(TAG + "error in read(): " + ex.getMessage());
        }
        return result;
    }
}
