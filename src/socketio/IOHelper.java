/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketio;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author datbt
 */
public class IOHelper {
    private final String TAG = "[IOHelper]\t";
    private final InputStream is;
    private final DataOutputStream dot;
    

    public IOHelper(InputStream is, OutputStream dot) {
        this.is = is;
        this.dot = new DataOutputStream(dot);
    }
    
    //write msg
    public boolean write(String msg){
        try {
            this.dot.writeUTF(msg);
        } catch (IOException ex) {
            System.err.println(TAG + "write() error: " + ex.getMessage());
            return false;
        }
        return true;
    }
    
    //read msg
    public String read(){
        String result = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        while (true){
            int r;
            try {
                r = is.read();
                if (r == -1)
                    break;
                
                baos.write(r);
                if (is.available() == 0){
                    result += new String(baos.toByteArray()).trim();
                    baos.reset();
                    return result;
                }
                
            } catch (IOException ex) {
                System.err.println(TAG + "read() error: " + ex.getMessage());
            }
        }
        return result;
    }
    
    public String read2(){
        String result = "";
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            int r = bis.read();
            while (r != -1){
                byte b = (byte)r;
                baos.write(b);
                r = bis.read();
            }
            result += new String(baos.toByteArray()).trim();
        } catch (IOException ex) {
            System.err.println(TAG + "read2() error: " + ex.getMessage());
        }
        
        return result;
    }
}
