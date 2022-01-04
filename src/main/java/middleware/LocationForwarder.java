package middleware;

import middleware.communication.message.ResponseMessage;
import org.json.JSONObject;

public class LocationForwarder {

    public static ResponseMessage delegate(Object remoteObjectId, JSONObject jsonObject) {
        JSONObject response = new JSONObject();
        response.append("Error: ", "Method not found");
        return new ResponseMessage("404", "Not Found", response.toString());
    }

}
