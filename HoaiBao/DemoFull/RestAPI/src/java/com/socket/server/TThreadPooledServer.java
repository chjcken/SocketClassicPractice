/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoaibao
 */
public class TThreadPooledServer implements Runnable {

    protected int serverPort = 1993;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;
    protected ExecutorService threadPool  = Executors.newFixedThreadPool(10);

    public TThreadPooledServer(int port) {
        this.serverPort = port;
    }

    public void run() {
          System.out.println("waiting for client");
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    break;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            try {
                this.threadPool.execute(new HandlerServer(clientSocket));
            } catch (IOException ex) {
                Logger.getLogger(TThreadPooledServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.");
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 1993", e);
        }
    }
    
    public static void main(String[] argv) {
        TThreadPooledServer s = new TThreadPooledServer(1993);
        new Thread(s).start();
    }
}
