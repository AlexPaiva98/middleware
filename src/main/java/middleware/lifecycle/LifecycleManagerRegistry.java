package middleware.lifecycle;

import java.util.HashMap;

/**
 * The LifecycleManagerRegistry class keeps track of all LIFECYCLE MANAGERS
 * in the system, as well as their relationship to remote objects and their servants.
 */
public class LifecycleManagerRegistry {

    private static HashMap<Strategy, LifecycleManager> lifecycleManagers = new HashMap<Strategy, LifecycleManager>();
    private static HashMap<Object, Strategy> remoteObjects = new HashMap<Object, Strategy>();

    public static LifecycleManager getLifecycleManager(Strategy strategy) {
        return lifecycleManagers.get(strategy);
    }

    public static LifecycleManager getLifecycleManager(Object remoteObjectId) {
        return lifecycleManagers.get(remoteObjects.get(remoteObjectId));
    }

    public static void registerLifecycleManager(Strategy strategy, LifecycleManager lifecycleManager) {
        lifecycleManagers.put(strategy, lifecycleManager);
    }

    public static void registerRemoteObject(Object remoteObjectId, Strategy strategy) {
        remoteObjects.put(remoteObjectId, strategy);
    }

}