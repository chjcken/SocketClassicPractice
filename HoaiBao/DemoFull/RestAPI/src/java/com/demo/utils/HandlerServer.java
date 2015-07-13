/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author hoaibao
 */
public class HandlerServer extends Thread {

    private Socket socket;
    private DataOutputStream sender;
    private DataInputStream reader;
    private int clientID;

    public HandlerServer(Socket s) throws IOException {
        socket = s;
        sender = new DataOutputStream(socket.getOutputStream());
        reader = new DataInputStream(socket.getInputStream());
        clientID = readInt();
        System.out.println("connected with client " + clientID);
    }

    @Override
    public void run() {
        int fileCount = 0;
        while (true) {
            try {
                String data = readString();
                if (data.equals("STOP") == true) {
                    stopService();
                    break;
                }
                if (data.equals("OK") == true) {
                    continue;
                }
                FileWriter fileWriter = new FileWriter(new File("client_" + clientID + "_" + fileCount + ".txt"));
                for (int i = 0; i < 1000; i++) {
                    fileWriter.write(data + "\n");
                }
                fileWriter.close();
                fileCount++;
                sendString("NEXT");
            } catch (Exception ex) {
                stopService();
            }

        }
    }

    public void stopService() {
        try {
            System.out.println("disconnected with client " + clientID);
            sender.close();
            reader.close();
            socket.close();
        } catch (IOException ex) {
        }
    }

    public String readString() {
        try {
            return reader.readUTF();
        } catch (IOException ex) {
            System.exit(0);
        }
        return "";
    }

    public int readInt() {
        try {
            return reader.readInt();
        } catch (IOException ex) {
            stopService();
        }
        return -1;
    }

    public void sendString(String s) {
        try {
            sender.writeUTF(s);
            sender.flush();
        } catch (IOException ex) {
            stopService();
        }
    }

}
