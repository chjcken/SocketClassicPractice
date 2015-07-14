/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyConcurrency;

import MyServerSide.HandlerServer;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phamdung
 */
public class MyWorker extends Thread {

    private MyThreadPool myThreadPool = null;
    private Lock lock;
    private Condition condition;
    private HandlerServer handlerServer = null;
    private int status = 0;
    private int WAITING = 2;
    private int NOT_INIT = 0;
    private int WORKING =1;
    private int STOP =3;
            
    /**
     * create and init a Worker
     * @param mThP : the Thread pool that this Worker is belong to
     */
    public MyWorker(MyThreadPool mThP) {
        myThreadPool = mThP;
        lock = new ReentrantLock();
        condition = lock.newCondition();
        System.out.println("created");
    }

    @Override
    public void run() {
        while (true) {

            try {
                lock.lock();
                handlerServer = myThreadPool.getHandler();
                if (handlerServer == null) {
                    status = WAITING;
                    condition.await();
                    continue;
                }
                status = WORKING;
                System.out.println("run");
                executeBusinessMethod(handlerServer);
            } catch (InterruptedException ex) {
                Logger.getLogger(MyWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * checking the status is waiting or not
     * @return : true if worker is waiting, false otherwise
     */
    public boolean isWaiting()
    {
        if(status == WAITING)
            return true;
        else
            return false;
    }
    
    /**
     * checking the status is working or not
     * @return : true if worker is working, false otherwise
     */
    public boolean isWorking()
    {
        if(status == WORKING)
            return true;
        else
            return false;
    }
    
    /**
     * awake this worker
     */
    public void awake()
    {
        condition.signal();
    }
    
    /**
     * doing something
     * 
     * @param handler 
     */
    private void executeBusinessMethod(HandlerServer handler) {
        handler.clientID = handler.readInt();
        //System.out.println("connected with client "+handler.clientID);
        int  fileCount =0;
        while(true)
        {
            
            try {
                String data = handler.readString();
                if(data.equals("STOP")==true)
                {
                    handler.stopService();
                    break;
                }
                if(data.equals("OK")==true)
                {
                    continue;
                }
                FileWriter fileWriter = new FileWriter(new File("client_"+handler.clientID+"_"+fileCount+".txt"));
                for(int i=0;i<1000;i++)
                {
                    fileWriter.write(data+"\n");
                }
                fileWriter.close();
                fileCount++;
                handler.sendString("NEXT");
                } catch (Exception ex) {
                handler.stopService();
            }
            
        }
    }

}
