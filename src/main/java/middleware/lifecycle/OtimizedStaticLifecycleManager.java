package middleware.lifecycle;

import middleware.RemoteObject;

public class OtimizedStaticLifecycleManager extends StaticLifecycleManager {

    public OtimizedStaticLifecycleManager() {
        super();
    }

    @Override
    public void registerRemoteObject(RemoteObject remoteObject) {
        // Add remote object to managed set
        this.remoteObjects.put(remoteObject.getId(), remoteObject);
        // Publish the remote object to the middleware available pool
        LifecycleManagerRegistry.registerRemoteObject(remoteObject.getId(), Strategy.OPTIMIZED_STATIC_INSTANCE);
    }

    // Used the pattern of LAZY ACQUISITION
    @Override
    public RemoteObject invocationArrived(Object remoteObjectId) {
        // Get remote object
        RemoteObject remoteObject = this.remoteObjects.get(remoteObjectId);
        // Check if there is already servant
        if (remoteObject.getInstance() == null) {
            // Create servant
            remoteObject.activate();
        }
        // Return servant
        return remoteObject;
    }

}