package middleware.lifecycle;

import middleware.RemoteObject;

import java.util.concurrent.ConcurrentHashMap;

public class StaticLifecycleManager implements LifecycleManager {

    // Number of instances is predetermined
    protected ConcurrentHashMap<Object, RemoteObject> remoteObjects;

    public StaticLifecycleManager() {
        this.remoteObjects = new ConcurrentHashMap<>();
    }

    @Override
    public void registerRemoteObject(RemoteObject remoteObject) {
        // Create servant
        remoteObject.activate();
        // Add remote object to managed set
        this.remoteObjects.put(remoteObject.getId(), remoteObject);
        // Publish the remote object to the middleware available pool
        LifecycleManagerRegistry.registerRemoteObject(remoteObject.getId(), Strategy.STATIC_INSTANCE);
    }

    @Override
    public RemoteObject invocationArrived(Object remoteObjectId) {
        // Return servant
        return this.remoteObjects.get(remoteObjectId);
    }

}