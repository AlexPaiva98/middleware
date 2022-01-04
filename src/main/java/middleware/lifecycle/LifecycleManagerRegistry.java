package middleware.lifecycle;

import java.util.HashMap;

public class LifecycleManagerRegistry {

    private static HashMap<Strategy, LifecycleManager> lifecycleManagers = new HashMap<Strategy, LifecycleManager>();

    public static LifecycleManager getLifecycleManager(Strategy strategy) {
        return lifecycleManagers.get(strategy);
    }

    public static LifecycleManager getLifecycleManager(Object remoteObjectId) {
        LifecycleManager lifecycleManager = null;
        for (Strategy strategy : lifecycleManagers.keySet()) {
            LifecycleManager temporary = lifecycleManagers.get(strategy);
            if (temporary.getRemoteObject(remoteObjectId) != null) {
                lifecycleManager = temporary;
                break;
            }
        }
        return lifecycleManager;
    }

    public static void registerLifecycleManager(Strategy strategy, LifecycleManager lifecycleManager) {
        lifecycleManagers.put(strategy, lifecycleManager);
    }


}