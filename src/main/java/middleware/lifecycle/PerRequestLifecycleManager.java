package middleware.lifecycle;

import middleware.RemoteObject;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

public class PerRequestLifecycleManager implements LifecycleManager {

    private int maxPools;
    private ConcurrentHashMap<Object, Pool> pools;

    public PerRequestLifecycleManager() {
        this.maxPools = 1;
        this.pools = new ConcurrentHashMap<>();
    }

    public int getMaxPools() {
        return this.maxPools;
    }

    public void setMaxPools(int maxPools) {
        this.maxPools = maxPools;
    }

    public void registerPerRequestInstancePool(RemoteObject remoteObject, int numberOfInstances) {
        Pool pool = new Pool(remoteObject, numberOfInstances);
        this.pools.put(remoteObject.getId(), pool);
    }

    @Override
    public void registerRemoteObject(RemoteObject remoteObject) {
        this.registerPerRequestInstancePool(remoteObject, this.getMaxPools());
        LifecycleManagerRegistry.registerRemoteObject(remoteObject.getId(), Strategy.PER_REQUEST);
    }

    @Override
    public RemoteObject invocationArrived(Object remoteObjectId) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Pool pool = this.pools.get(remoteObjectId);
        RemoteObject servant = pool.getFreeInstance();
        pool.removeFromPool(servant);
        servant.activate();
        return servant;
    }

    @Override
    public void invocationDone(RemoteObject servant) {
        Pool pool = this.pools.get(servant.getId());
        servant.deactivate();
        pool.putBackToPool(servant);
    }

}
