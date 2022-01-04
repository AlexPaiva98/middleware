package middleware.lifecycle;

import middleware.RemoteObject;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

public class StaticInstanceLifecycleManager implements LifecycleManager {

    private ConcurrentHashMap<Object, RemoteObject> remoteObjects;

    public StaticInstanceLifecycleManager() {
        this.remoteObjects = new ConcurrentHashMap<>();
    }

    @Override
    public RemoteObject getRemoteObject(Object id) {
        RemoteObject remoteObject = null;
        int index = 0;
        int size = this.remoteObjects.size();
        while (index < size) {
            remoteObject = this.remoteObjects.get(id);
            if (remoteObject != null) {
                break;
            }
            index++;
        }
        return remoteObject;
    }

    @Override
    public void registerRemoteObject(RemoteObject remoteObject) {
        this.remoteObjects.put(remoteObject.getId(), remoteObject);
    }

    @Override
    public RemoteObject invocationArrived(Object remoteObjectId) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        RemoteObject remoteObject = this.remoteObjects.get(remoteObjectId);
        if (remoteObject.getInstance() == null) {
            remoteObject.activate();
        }
        return remoteObject;
    }

}