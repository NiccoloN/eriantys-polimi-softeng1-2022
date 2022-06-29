package it.polimi.ingsw2022.eriantys.messages;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents a message sento to the client that has a timer, if the timer runs out a runnable task starts.
 * Every timed message has a lockId, given and retrievable from the server, in order to identify and accept the message
 * once it's received by the client.
 * @author Emanuele Musto
 * @author Niccolò Nicolosi
 */
public abstract class TimedMessage extends Message {
    
    private final boolean fromClient;
    private final int lockId;
    
    public TimedMessage() {
        
        fromClient = false;
        lockId = EriantysServer.getInstance().getNextMessageLockId();
    }
    
    public TimedMessage(boolean fromClient) {
    
        this.fromClient = fromClient;
        lockId = EriantysClient.getInstance().getNextMessageLockId();
    }
    
    /**
     * Method called by the server when a message is sent, and defines for every timed message
     * the timeout and the task that will run once the timeout expires (ex. server shutdown).
     */
    public abstract void waitForValidResponse() throws InterruptedException;
    
    /**
     * Method called by the implementations of the waitForValidResponse(). It actually waits for a response
     * by getting an id from the server to synchronize on, and waits for the amount of time specified by the parameter
     * timeout. At the end of the wait, if the lock (a boolean) is still set to falso, the timeout task will run.
     * Otherwise, everyone waiting on the lock is notified, and the lock is set free for others to use.
     * @param timeout     the amount of time for the wait
     * @param timeoutTask the task to run when the timeout expires.
     * @throws InterruptedException if the thread waiting is interrupted.
     */
    protected void waitForValidResponse(int timeout, Runnable timeoutTask) throws InterruptedException {
        
        EriantysServer server = null;
        EriantysClient client = null;
        AtomicBoolean lock;
    
        if(fromClient) {
            
            client = EriantysClient.getInstance();
            lock = client.getMessageLock(lockId);
        }
        else {
            
            server = EriantysServer.getInstance();
            lock = server.getMessageLock(lockId);
        }
        
        synchronized(lock) {
            
            lock.wait(timeout * 1000L);
            if(!lock.get()) timeoutTask.run();
            
            if(fromClient) client.removeMessageLock(lockId);
            else server.removeMessageLock(lockId);
            
            lock.notifyAll();
        }
    }
    
    /**
     * Accept the response by retrieving the lock from the server with the right id, by setting the lock to true,
     * and by notifying the waiting thread.
     * (Setting the lock to true means that the thread waiting for response does not perform the timeout task)
     */
    public void acceptResponse() {
        
        AtomicBoolean lock;
    
        if(fromClient) lock = EriantysClient.getInstance().getMessageLock(lockId);
        else lock = EriantysServer.getInstance().getMessageLock(lockId);
        
        synchronized(lock) {
            
            lock.set(true);
            lock.notifyAll();
        }
    }
}
