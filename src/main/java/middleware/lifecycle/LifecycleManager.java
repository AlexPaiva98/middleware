package middleware.lifecycle;

import middleware.RemoteObject;

public interface LifecycleManager {

    public void registerRemoteObject(RemoteObject remoteObject);

    public RemoteObject invocationArrived(Object remoteObjectId);

    public default void invocationDone(RemoteObject remoteObject) {}

}
