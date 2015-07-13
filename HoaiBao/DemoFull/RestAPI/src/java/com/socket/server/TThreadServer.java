/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socket.server;

import com.demo.utils.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bao
 */
public class TThreadServer implements Runnable {

    protected boolean isStopped = false;
    private ServerSocket server;
    private int port = 1993;

    public TThreadServer() {
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(TThreadServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.server.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    @Override
    public synchronized void run() {
        System.out.println("waiting for client");
        while (!isStopped) {
            try {
                new HandlerServer(server.accept()).start();
            } catch (IOException ex) {
                Logger.getLogger(TThreadServer.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error accept client !!!");
            }
        }
    }

    public static void main(String[] argv) {
        TThreadServer s = new TThreadServer();
        new Thread(s).start();
    }
}
