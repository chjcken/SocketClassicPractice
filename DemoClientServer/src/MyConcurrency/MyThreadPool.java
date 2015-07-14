/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyConcurrency;

import MyServerSide.HandlerServer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phamdung
 */
public class MyThreadPool extends Thread {

    public volatile int numOfWorker = 0;
    public volatile int numOfHandler = 0;
    public volatile int numOfWorkerStarted = 0;
    public volatile int numOfWorkerWorking = 0;
    private volatile BlockingQueue<HandlerServer> queueOfHandler = null;
    public volatile List<MyWorker> listOfWorker = null;
    private int coreSize = 0;
    private int maxSize = 0;
    private Lock lock;
    private Condition startCondition;

    /**
     * *
     * init my Thread pool
     *
     * @param core : number of thread are kept in my thread pool
     * @param max : the maximum of thread are allow to run in my thread pool
     */
    public MyThreadPool(int core, int max) {
        coreSize = core;
        maxSize = max;
        queueOfHandler = new LinkedBlockingQueue<HandlerServer>();
        listOfWorker = new ArrayList<MyWorker>();
        lock = new ReentrantLock();
        startCondition = lock.newCondition();
    }

    @Override
    public void run() {
        while (true) {
            try {
                lock.lock();
                try {
                    startCondition.await();
                    if ((numOfHandler > 0 && numOfHandler <= maxSize) && numOfWorkerStarted < coreSize) {
                        int numOfAllowingWoker = coreSize - numOfWorkerStarted;
                        for (int h = 0, w = 0; h < numOfHandler && w < numOfAllowingWoker; h++, w++) {
                            createAndStartNewWorker();
                        }
                        continue;
                    }
                    if(numOfHandler>maxSize && numOfWorkerStarted<maxSize){
                        int numOfAllowingWoker = maxSize - numOfWorkerStarted;
                        for (int h = 0, w = 0; h < numOfHandler && w < numOfAllowingWoker; h++, w++) {
                            createAndStartNewWorker();
                        }
                        continue;
                    }
                    int numOfWorkerWorking = getNumOfWorkerWorking();
                    if ((numOfHandler > 0 && numOfHandler <= maxSize) && numOfWorkerWorking < coreSize) {
                        int numOfAllowingWoker = coreSize - numOfWorkerWorking;
                        for (int h = 0, w = 0; h < numOfHandler && w < numOfAllowingWoker; h++, w++) {
                            for(MyWorker worker : listOfWorker)
                                {
                                    if(worker.isWaiting()==true)
                                    {
                                        worker.awake();
                                        break;
                                    }
                                }
                        }
                        continue;
                    }
                    if (numOfHandler > maxSize && numOfWorkerWorking < maxSize) {
                        int numOfAllowingWoker = maxSize - numOfWorkerWorking;
                        for (int h = 0, w = 0; h < numOfHandler && w < numOfAllowingWoker; h++, w++) {
                            for(MyWorker worker : listOfWorker)
                                {
                                    if(worker.isWaiting()==true)
                                    {
                                        worker.awake();
                                        break;
                                    }
                                }
                        }
                        continue;
                    }
                    
                    
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MyThreadPool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * traveling all worker and count number of workers are waiting
     * @return : number of workers are waiting
     */
    private int getNumOfWorkerWorking()
    {
        int count=0;
        for(MyWorker worker : listOfWorker)
        {
            if(worker.isWorking()==true)
            {
                count++;
            }
        }
        return count;
    }
    
    /**
     * *
     * this method is called by Worker to return Handler for worker resolve
     *
     * @return
     */
    public HandlerServer getHandler() throws InterruptedException {
        HandlerServer sh = queueOfHandler.poll();
        numOfHandler--;
        return sh;
    }

    /**
     * create new worker for resolve a Handler
     */
    public void createAndStartNewWorker() {
        MyWorker w = new MyWorker(this);
        listOfWorker.add(w);
        w.start();
        numOfWorkerStarted++;
    }

    /**
     * *
     * this method is called by server to add a new Handler
     *
     * @param hdler : the new Handler is add to queue
     */
    public void execute(HandlerServer hdler) {
        queueOfHandler.add(hdler);
        incHandler();
    }

    /**
     * *
     * increase number of handler by 1 when a new Handler is added to queue
     */
    public void incHandler() {
        lock.lock();
        try {
            numOfHandler++;
            startCondition.signal();
        } finally {
            lock.unlock();
        }
    }
}
