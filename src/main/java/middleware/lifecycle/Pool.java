package middleware.lifecycle;

import middleware.RemoteObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Pool {

    private List<RemoteObject> instances;
    private Lock look;
    private Condition isEmpty;

    public Pool(RemoteObject remoteObject, int number) {
        this.instances = new ArrayList<RemoteObject>();
        for (int i = 0; i < number; i++) {
            this.addPoolInstance(remoteObject);
        }
        this.look = new ReentrantLock(true);
        this.isEmpty = this.look.newCondition();
    }

    public void addPoolInstance(RemoteObject remoteObject) {
        this.instances.add(new RemoteObject(remoteObject.getId(), remoteObject.getMethod()));
    }

    public RemoteObject getFreeInstance() {
        RemoteObject remoteObject = null;
        this.look.lock();
        try {
            while(this.instances.size() == 0) {
                isEmpty.await();
            }
            remoteObject = instances.get(0);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } finally {
            look.unlock();
        }
        return remoteObject;
    }

    public void removeFromPool(RemoteObject remoteObject) {
        this.look.lock();
        try {
            this.instances.remove(remoteObject);
        } finally {
            this.look.unlock();
        }
    }

    public void putBackToPool(RemoteObject remoteObject) {
        this.look.lock();
        try {
            this.instances.add(remoteObject);
            this.isEmpty.signal();
        } finally {
            this.look.unlock();
        }
    }

}