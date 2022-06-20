package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class TimedMessage extends ToClientMessage {

    private final int lockId;

    public TimedMessage() {

        lockId = EriantysServer.getInstance().getNextMessageLockId();
    }

    public abstract void waitForValidResponse() throws InterruptedException;

    protected void waitForValidResponse(int timeout, Runnable timeoutTask) throws InterruptedException {

        EriantysServer server = EriantysServer.getInstance();
        AtomicBoolean lock = server.getMessageLock(lockId);

        synchronized (lock) {
            lock.wait(timeout * 1000L);
            if(!lock.get()) timeoutTask.run();
            server.removeMessageLock(lockId);
            lock.notifyAll();
        }
    }

    public void acceptResponse() {

        AtomicBoolean lock = EriantysServer.getInstance().getMessageLock(lockId);

        synchronized (lock) {
            lock.set(true);
            lock.notifyAll();
        }
    }
}
