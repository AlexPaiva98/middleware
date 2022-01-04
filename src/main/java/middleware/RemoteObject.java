package middleware;

import lombok.extern.slf4j.Slf4j;
import middleware.communication.message.ResponseMessage;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/*
 *  Remote objects have a unique OBJECT ID in their local address space, as
	well as a means to construct an ABSOLUTE OBJECT REFERENCE . The ABSO -
	LUTE OBJECT REFERENCE is used to reference and subsequently access a
	remote object across the network.
 */

@Slf4j
public class RemoteObject {

    private Object id;
    private Method method;
    private Object instance;

    public Object getId() {
        return this.id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Method getMethod() {
        return this.method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getInstance() {
        return this.instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public void activate() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> clazz = this.method.getDeclaringClass();
        this.instance = clazz.getDeclaredConstructor().newInstance();
    }

    public void deactivate() {
        this.instance = null;
    }

    public ResponseMessage executeOperation(JSONObject jsonObject) {
        try {
            JSONObject obj = (JSONObject) this.method.invoke(this.instance, jsonObject);
            ResponseMessage message = new ResponseMessage("200", "OK", obj.toString());
            return message;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            JSONObject response = new JSONObject();
            response.append("Error: ", "An error occurred while processing the method.");
            return new ResponseMessage("500", "Internal Server Error", response.toString());
        }
    }

}