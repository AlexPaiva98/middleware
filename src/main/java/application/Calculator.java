package application;

import middleware.annotations.*;
import middleware.lifecycle.Strategy;
import org.json.JSONObject;
import lombok.NoArgsConstructor;

/**
 * Class calculator, implemented by the client using the annotations
 * implemented by our middleware. Note that the return of all methods
 * is a JSONObject.
 */

@NoArgsConstructor


// RequestMap annotation, the attribute "router" is what sets the class route
@RequestMap(router = "/calc")
public class Calculator {

    // Lifecycle annotation, the "strategy" attribute is what defines the lifecycle management approach
    @Lifecycle(strategy = Strategy.STATIC_INSTANCE)
    //Get method, the attribute "router" is what sets the endpoint route
    @Get(router = "/add")
    public JSONObject add(JSONObject jsonObject) throws Throwable {

        //Get the variables from JSON
        float a = jsonObject.getFloat("var1");
        float b = jsonObject.getFloat("var2");

        //Build the return JSON
        JSONObject result = new JSONObject();
        result.put("result", a + b);

        return result;
    }

    // Lifecycle annotation, the "strategy" attribute is what defines the lifecycle management approach
    @Lifecycle(strategy = Strategy.OPTIMIZED_STATIC_INSTANCE)
    //Post method, the attribute "router" is what sets the endpoint route
    @Post(router = "/sub")
    public JSONObject sub(JSONObject jsonObject) throws Throwable {
        //Get the variables from JSON
        float a = jsonObject.getFloat("var1");
        float b = jsonObject.getFloat("var2");
        //Build the return JSON
        JSONObject result = new JSONObject();
        result.put("result", a - b);

        return result;
    }

    // Lifecycle annotation, the "strategy" attribute is what defines the lifecycle management approach
    @Lifecycle(strategy = Strategy.PER_REQUEST_INSTANCE)
    //Put method, the attribute "router" is what sets the endpoint route
    @Put(router = "/mul")
    public JSONObject mul(JSONObject jsonObject) throws Throwable {
        //Get the variables from JSON
        float a = jsonObject.getFloat("var1");
        float b = jsonObject.getFloat("var2");
        //Build the return JSON
        JSONObject result = new JSONObject();
        result.put("result", a * b);

        return result;
    }

    // Lifecycle annotation, the "strategy" attribute is what defines the lifecycle management approach
    @Lifecycle(strategy = Strategy.OPTIMIZED_PER_REQUEST_INSTANCE)
    // Pool annotation, the "maxQuantity" attribute defines the maximum number of instances of a remote object
    @Pool(maxQuantity = 50)
    //Delete method, the attribute "router" is what sets the endpoint route
    @Delete(router = "/div")
    public JSONObject div(JSONObject jsonObject) throws Throwable {
        //Get the variables from JSON
        float a = jsonObject.getFloat("var1");
        float b = jsonObject.getFloat("var2");
        //Build the return JSON
        JSONObject result = new JSONObject();
        result.put("result", a / b);

        return result;
    }
}
