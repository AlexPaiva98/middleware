package middleware;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

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
        LifecycleManagerRegistry.registerLifecycleManager(Strategy.STATIC_INSTANCE, new StaticLifecycleManager());
        LifecycleManagerRegistry.registerLifecycleManager(Strategy.OPTIMIZED_STATIC_INSTANCE, new OtimizedStaticLifecycleManager());
        LifecycleManagerRegistry.registerLifecycleManager(Strategy.PER_REQUEST_INSTANCE, new PerRequestLifecycleManager());
        LifecycleManagerRegistry.registerLifecycleManager(Strategy.OPTIMIZED_PER_REQUEST_INSTANCE, new OtimizedPerRequestLifecycleManager());
    }

    // calls the method that filters and saves remote objects
    public void registerRemoteObjects(Object object) {
        //	Extract the component
        Class<?> clazz = object.getClass();
        LifecycleManager lifecycleManager = null;
        for (Method method : clazz.getDeclaredMethods()) {
            lifecycleManager = this.filterLifecycle(method);
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

    public LifecycleManager filterLifecycle(Method method) {
        Lifecycle lifecycle = method.getAnnotation(Lifecycle.class);
        if (lifecycle== null) {
            return LifecycleManagerRegistry.getLifecycleManager(Strategy.STATIC_INSTANCE);
        } else {
            LifecycleManager lifecycleManager = LifecycleManagerRegistry.getLifecycleManager(lifecycle.strategy());
            if (lifecycleManager instanceof OtimizedPerRequestLifecycleManager) {
                Pool pool = method.getAnnotation(Pool.class);
                if (pool != null) {
                    ((OtimizedPerRequestLifecycleManager) lifecycleManager).setMaxPools(pool.maxQuantity());
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
