package middleware.lifecycle;

import middleware.RemoteObject;

import java.lang.reflect.InvocationTargetException;

public interface LifecycleManager {

    public void registerRemoteObject(RemoteObject remoteObject);

    public RemoteObject invocationArrived(Object remoteObjectId) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    public default void invocationDone(RemoteObject remoteObject) {}

}
