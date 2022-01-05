package middleware;

import java.lang.reflect.Method;

import middleware.annotations.*;
import middleware.annotations.Pool;
import middleware.lifecycle.*;

/*
 * Class that encapsulates the middleware, responsible
 *  for storing the methods in hashmaps and starting the
 *  server on the correct port.
 */
public class Autumn {

    public Autumn() {
        LifecycleManagerRegistry.registerLifecycleManager(Strategy.STATIC_INSTANCE, new StaticInstanceLifecycleManager());
        LifecycleManagerRegistry.registerLifecycleManager(Strategy.PER_REQUEST, new PerRequestLifecycleManager());
    }

    // calls the method that filters and saves remote objects
    public void registerRemoteObjects(Object object) {
        //	Extract the component
        Class<?> clazz = object.getClass();
        LifecycleManager lifecycleManager = filterLifecycle(clazz);
        for (Method method : clazz.getDeclaredMethods()) {
            RemoteObject remoteObject = new RemoteObject();
            if (method.isAnnotationPresent(Get.class)) {
                remoteObject.setId("get" + clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Get.class).router());
            } else if (method.isAnnotationPresent(Post.class)) {
                remoteObject.setId("post" + clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Post.class).router());
            } else if (method.isAnnotationPresent(Put.class)) {
                remoteObject.setId("put" + clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Put.class).router());
            } else if (method.isAnnotationPresent(Delete.class)) {
                remoteObject.setId("delete" + clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Delete.class).router());
            }
            method.setAccessible(true);
            remoteObject.setMethod(method);
            lifecycleManager.registerRemoteObject(remoteObject);
        }
    }

    public LifecycleManager filterLifecycle(Class<?> clazz) {
        if (clazz.getAnnotation(Lifecycle.class) == null) {
            return LifecycleManagerRegistry.getLifecycleManager(Strategy.STATIC_INSTANCE);
        } else {
            LifecycleManager lifecycleManager = LifecycleManagerRegistry.getLifecycleManager(clazz.getAnnotation(Lifecycle.class).strategy());
            if (lifecycleManager instanceof PerRequestLifecycleManager) {
                Pool pool = clazz.getAnnotation(Pool.class);
                if (pool != null) {
                    ((PerRequestLifecycleManager) lifecycleManager).setMaxPools(pool.maxQuantity());
                }
            }
            return lifecycleManager;
        }
    }

    //	Method that starts the server on the chosen port
    public void start(int port) {
        // ServerRequestHandler instance on the chosen port
        ServerRequestHandler server = new ServerRequestHandler(port);
        // call start method
        server.run();
    }

}
