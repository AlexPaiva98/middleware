package middleware;

import java.lang.reflect.InvocationTargetException;

import middleware.communication.message.InternMessage;
import middleware.communication.message.ResponseMessage;
import middleware.lifecycle.LifecycleManager;
import middleware.lifecycle.LifecycleManagerRegistry;

/**
 * Provide an INVOKER that accepts client invocations from REQUESTORS.
 * REQUESTORS send requests across the network, containing the ID of
 * the remote object, operation name, operation parameters, as well as
 * additional contextual information. The INVOKER reads the request
 * and demarshals it to obtain the OBJECT ID and the name of the operation.
 * It then dispatches the invocation with demarshaled invocation
 * parameters to the targeted remote object. That is, it looks up the
 * correct local object and its operation implementation, as described by
 * the remote invocation, and invokes it.
 */
public class Invoker {
    // Method that invokes a remote object, receiving an InternMessage and returning a ResponseMessage
    public ResponseMessage invokeRemoteObject(InternMessage msg) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        ResponseMessage respMsg = null;
        // Forming the remote object id
        String remoteObjectId = msg.getMethodType().toLowerCase() + msg.getRoute();
        // Search for cycle manager responsible for remote object
        LifecycleManager lifecycleManager = LifecycleManagerRegistry.getLifecycleManager(remoteObjectId);
        /**
         * In the event that a target remote object cannot be found by the INVOKER ,
         * the INVOKER can delegate dispatching to a LOCATION FORWARDER .
         */
        if (lifecycleManager == null) {
            respMsg = LocationForwarder.delegate(remoteObjectId, msg.getBody());
        } else {
            // Seek remote object
            RemoteObject remoteObject = lifecycleManager.invocationArrived(remoteObjectId);
            // Calls the invoke method passing data.
            respMsg = remoteObject.executeOperation(msg.getBody());
            // Release remote object
            lifecycleManager.invocationDone(remoteObjectId);
        }
        return respMsg;
    }
}
