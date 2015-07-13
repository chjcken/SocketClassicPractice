/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.demo.utils.Utility;
import com.rest.services.FileUploadDownloadService;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dung
 */
public class Client extends Thread {

    private Socket socket;
    private String serverAddress = "localhost";
    private int port = 1993;
    private int clientID;
    private String message;
    private DataOutputStream sender;
    private DataInputStream reader;
    public static List<String> dsKQ = new ArrayList<>();

    public Client(int ID) {
        clientID = ID;
    }

    private Client(int i, String message) {
        clientID = i;
        this.message = message;
    }

    public void connect() {
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

    public void stopService() {
        try {
            sender.close();
            reader.close();
            socket.close();
        } catch (IOException ex) {
        }
    }

    public void sendString(String s) {
        try {
            sender.writeUTF(s);
        } catch (IOException ex) {
            stopService();
        }
    }

    public void sendInt(int i) {
        try {
            sender.writeInt(i);
            sender.flush();
        } catch (IOException ex) {
            stopService();
        }
    }

    public String readString() {
        try {
            return reader.readUTF();
        } catch (IOException ex) {
            stopService();
        }
        return "";
    }

    @Override
    public void run() {
        int i = 0;
        long timeStart = System.currentTimeMillis();
        while (i < clientID) {
            sendString(message);
            if (i > clientID) {
                sendString("OK");
            }
            i++;
            readString();
        }
        long timeEnd = System.currentTimeMillis();
        String messageOut = "client " + clientID + " done with " + clientID + " times in " + (timeEnd - timeStart) + "ms";
        System.out.println(messageOut);
        dsKQ.add(messageOut);
        sendString("STOP");
    }

    public static int RunClient() throws InterruptedException {
        List<Client> listClient = new ArrayList<Client>();
        try {
            List<String> strs = Utility.ReadFileClient();
            int clientNumber = Integer.parseInt(strs.get(0));
            String message = strs.get(1);
            for (int i = 1; i <= clientNumber; i++) {
                Client cl = new Client(i, message);
                listClient.add(cl);
                cl.connect();
                cl.start();
            }
            for (Client c : listClient) {
                if (c.isAlive()) {
                    c.join();
                }
            }
            Utility.WriteFileResult(dsKQ, FileUploadDownloadService.FILE_PATH);
            dsKQ.clear();
            return 1;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
