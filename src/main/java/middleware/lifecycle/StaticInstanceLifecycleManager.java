package middleware.lifecycle;

import middleware.RemoteObject;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

public class StaticInstanceLifecycleManager implements LifecycleManager {

    /**
     * Number of instances is predetermined
     */
    private ConcurrentHashMap<Object, RemoteObject> remoteObjects;

    public StaticInstanceLifecycleManager() {
        this.remoteObjects = new ConcurrentHashMap<>();
    }

    @Override
    public RemoteObject getRemoteObject(Object id) {
        return this.remoteObjects.get(id);
    }

    @Override
    public void registerRemoteObject(RemoteObject remoteObject) {
        this.remoteObjects.put(remoteObject.getId(), remoteObject);
        LifecycleManagerRegistry.registerRemoteObject(remoteObject.getId(), Strategy.STATIC_INSTANCE);
    }

    /**
     * Used the pattern of LAZY ACQUISITION
     */
    @Override
    public RemoteObject invocationArrived(Object remoteObjectId) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        RemoteObject remoteObject = this.remoteObjects.get(remoteObjectId);
        if (remoteObject.getInstance() == null) {
            remoteObject.activate();
        }
        return remoteObject;
    }

}