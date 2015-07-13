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
 * @author Dung
 */
public class Server {

    private ServerSocket server;
    private int port = 1993;

    public Server() {
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void serve() {
        System.out.println("waiting for client");
        while (true) {
            try {
                new HandlerServer(server.accept()).start();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("ERROR!Couldn't accept client !!!");
            }
        }
    }

    public static void main(String s[]) {
        Server server = new Server();
        server.serve();
    }
}
