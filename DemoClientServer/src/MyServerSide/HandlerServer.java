/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author phamdung
 */
public class HandlerServer {
    
    private Socket socket;
    private DataOutputStream sender;
    private DataInputStream reader;
    public int clientID;
    
    /**
     * create a handlerServer on a socket
     * this function is called by server
     * @param s : the socket is bounded on by this HandlerServer
     * @throws IOException 
     */
    public HandlerServer(Socket s) throws IOException
    {
        socket = s;
        sender = new DataOutputStream(socket.getOutputStream());
        reader = new DataInputStream(socket.getInputStream());
    }
    
    /**
     * read a string from client
     * @return : the string is read from client
     */
    public String readString()
    {
        try {
            return reader.readUTF();
        } catch (IOException ex) {
            System.exit(0);
        }
        return "";
    }
    
    /**
     * read a int number from client
     * @return : the int number is read from client
     */
    public int readInt()
    {
        try {
            return reader.readInt();
        } catch (IOException ex) {
                stopService();
        }
        return -1;
    }
    
    /**
     * send a string to client
     * @param s : string, that want to be sent
     */
    public void sendString(String s)
    {
        try {
            sender.writeUTF(s);
            sender.flush();
        } catch (IOException ex) {
                stopService();
        }
    }
    
    /**
     * close stream and disconnect with client
     */
    public void stopService()
    {
        try {
            //System.out.println("disconnected with client "+clientID);
            sender.close();
            reader.close();
            socket.close();
        } catch (IOException ex) {
        }
    }
    
}
