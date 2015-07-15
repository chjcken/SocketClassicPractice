/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketio;

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
    private final DataOutputStream dos;
    

    public IOHelper(InputStream is, OutputStream os) {
        this.is = is;
        this.dos = new DataOutputStream(os);
    }
    
    //write msg
    public boolean write(String msg){
        try {
            //this.dos.writeUTF(msg);
            this.dos.write(msg.getBytes());
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
                    break;
                }
                
            } catch (IOException ex) {
                System.err.println(TAG + "read() error: " + ex.getMessage());                
                result = "";
                break;
            }
        }
        return result;
    }
    
    public void close(){
        try {
            if (is != null)            
                is.close();
            if (dos != null)
                dos.close();
            
        } catch (IOException ex) {
            //Logger.getLogger(IOHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    public String read2(){
//        String result = "";
//        BufferedInputStream bis = new BufferedInputStream(is);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            int r = bis.read();
//            while (r != -1){
//                byte b = (byte)r;
//                baos.write(b);
//                r = bis.read();
//            }
//            result += new String(baos.toByteArray()).trim();
//        } catch (IOException ex) {
//            System.err.println(TAG + "read2() error: " + ex.getMessage());
//        }
//        
//        return result;
//    }
}
