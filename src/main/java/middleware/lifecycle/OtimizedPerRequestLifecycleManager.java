package middleware.lifecycle;

import middleware.RemoteObject;

import java.util.concurrent.ConcurrentHashMap;

public class OtimizedPerRequestLifecycleManager implements LifecycleManager {

    // Maximum amount of instances
    private int maxPools;
    // Remote object mapping with its instance pool
    private ConcurrentHashMap<Object, Pool> pools;

    public OtimizedPerRequestLifecycleManager() {
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
        // Create pool
        Pool pool = new Pool(remoteObject, numberOfInstances);
        // Register pool in the managed set
        this.pools.put(remoteObject.getId(), pool);
    }

    @Override
    public void registerRemoteObject(RemoteObject remoteObject) {
        // Create remote object pool
        this.registerPerRequestInstancePool(remoteObject, this.getMaxPools());
        // Publish the remote object to the middleware available pool
        LifecycleManagerRegistry.registerRemoteObject(remoteObject.getId(), Strategy.OPTIMIZED_PER_REQUEST_INSTANCE);
    }

    @Override
    public RemoteObject invocationArrived(Object remoteObjectId) {
        // Get pool from remote object
        Pool pool = this.pools.get(remoteObjectId);
        // Get a servant
        RemoteObject servant = pool.getFreeInstance();
        // Remove servant from pool
        pool.removeFromPool(servant);
        // Activate servant
        servant.activate();
        // Return servant
        return servant;
    }

    @Override
    public void invocationDone(RemoteObject servant) {
        // Get pool from remote object
        Pool pool = this.pools.get(servant.getId());
        // Deactivate servant
        servant.deactivate();
        // Put servant back in poll
        pool.putBackToPool(servant);
    }

}
