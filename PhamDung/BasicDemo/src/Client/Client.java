/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dung
 */
public class Client extends Thread{

    private Socket socket;
    private String serverAddress = "localhost";
    private int port = 1993;
    private int clientID;
    private DataOutputStream sender;
    private DataInputStream reader;
    public Client(int ID) {
        clientID = ID;
    }
    public void connect()
    {
        try {
            socket = new Socket(serverAddress, port);
            sender = new DataOutputStream(socket.getOutputStream());
            reader = new DataInputStream(socket.getInputStream());
            sendInt(clientID);
        } catch (IOException ex) {
            System.out.println("Couldn't connect to server ");
            this.stop();
        }
    }
    public void stopService()
    {
        try {
            sender.close();
            reader.close();
            socket.close();
        } catch (IOException ex) {
        }
    }
    public void sendString(String s)
    {
        try {
            sender.writeUTF(s);
        } catch (IOException ex) {
            stopService();
        }
    }
    public void sendInt(int i)
    {
        try {
            sender.writeInt(i);
            sender.flush();
        } catch (IOException ex) {
            stopService();
        }
    }
    public String readString()
    {
        try {
            return reader.readUTF();
        } catch (IOException ex) {
            stopService();
        }
        return "";
    }
    @Override
    public void run() {
        int i=0;
        long timeStart = System.currentTimeMillis();
        while(i<clientID)
        {
            sendString("i issue this is a 100 chars string ");
            if(i>clientID)
            {
                sendString("OK");
            }
            i++;
            readString();
        }
        long timeEnd = System.currentTimeMillis();
        System.out.println("client "+ clientID+" done with "+clientID+" times in "+(timeEnd-timeStart)+"ms");
        sendString("STOP");
    }
    
    
    public static void main(String[] args) {
        for(int i=1;i<10;i++)
        {
            Client cl = new Client(i);
            cl.connect();
            cl.start();
        }
        
    }
    
}
