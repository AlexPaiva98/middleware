package middleware.communication.message;

import lombok.Data;
import org.json.JSONObject;

@Data
public class InternMessage {

    private String route;
    private String methodType;
    private JSONObject body;
    private MessageType type;

    public String getReference() {
        return this.getMethodType().toLowerCase() + this.getRoute();
    }

}
