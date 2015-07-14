/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyServerSide;

import MyConcurrency.MyThreadPool;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phamdung
 */
public class MyServer {

    private ServerSocket server;
    private int port = 1993;
    private MyThreadPool myThreadPool = null;
    /**
     * create a Server Socket and init thread pool with core and max of threads are allow to run
     * @param core : number of worker are kept running on thread pool
     * @param max : number of worker are allowed running on thread pool
     */
    public MyServer(int core,int max)
    {
        try {
            server = new ServerSocket(port);
            myThreadPool = new MyThreadPool(core, max);
            myThreadPool.start();
        } catch (IOException ex) {
            Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * starting to receive connect from client
     */
    public void serve() 
    {
        System.out.println("waiting for client");
        while(true)
        {
            try {
                HandlerServer handlerServer = new HandlerServer(server.accept());
                myThreadPool.execute(handlerServer);
                //System.out.println("put to queue");
            } catch (IOException ex) {
                Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("ERROR!Couldn't accept client !!!");
            }
        }
    }
    
    public static void main(String s[])
    {
        MyServer server = new MyServer(4,10);
        server.serve();
    }
    
}
