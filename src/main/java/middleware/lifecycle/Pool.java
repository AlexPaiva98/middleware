package middleware.lifecycle;

import middleware.RemoteObject;

import java.util.ArrayList;
import java.util.List;

public class Pool {

    private List<RemoteObject> instances;

    public Pool(RemoteObject remoteObject, int number) {
        this.instances = new ArrayList<RemoteObject>();
        for (int i = 0; i < number; i++) {
            this.addPoolInstance(remoteObject);
        }
    }

    public void addPoolInstance(RemoteObject remoteObject) {
        this.instances.add(new RemoteObject(remoteObject.getId(), remoteObject.getMethod()));
    }

    public synchronized RemoteObject getFreeInstance() {
        return instances.get(0);
    }

    public synchronized void removeFromPool(RemoteObject remoteObject) {
        this.instances.remove(remoteObject);
    }

    public synchronized void putBackToPool(RemoteObject remoteObject) {
        this.instances.add(remoteObject);
    }

}