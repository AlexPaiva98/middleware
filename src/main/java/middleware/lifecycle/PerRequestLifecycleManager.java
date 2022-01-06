package middleware.lifecycle;

import middleware.RemoteObject;

import java.util.concurrent.ConcurrentHashMap;

public class PerRequestLifecycleManager implements LifecycleManager {

    protected ConcurrentHashMap<Object, RemoteObject> remoteObjects;

    public PerRequestLifecycleManager() {
        this.remoteObjects = new ConcurrentHashMap<>();
    }

    @Override
    public void registerRemoteObject(RemoteObject remoteObject) {
        // Add remote object to managed set
        this.remoteObjects.put(remoteObject.getId(), remoteObject);
        // Publish the remote object to the middleware available pool
        LifecycleManagerRegistry.registerRemoteObject(remoteObject.getId(), Strategy.PER_REQUEST_INSTANCE);
    }

    @Override
    public RemoteObject invocationArrived(Object remoteObjectId) {
        // Get remote object
        RemoteObject remoteObject = this.remoteObjects.get(remoteObjectId);
        // Create servant
        RemoteObject servant = new RemoteObject(remoteObject.getId(), remoteObject.getMethod());
        servant.activate();
        // Return servant
        return servant;
    }

    @Override
    public void invocationDone(RemoteObject servant) {
        // Destroy servant
        servant.deactivate();
    }

}
