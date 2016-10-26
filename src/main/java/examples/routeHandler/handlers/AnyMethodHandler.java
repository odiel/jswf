package examples.routeHandler.handlers;

import jswf.commons.components.http.routeHandlerComponent.Request;
import jswf.commons.components.generic.RequestHandlerInterface;
import jswf.commons.components.http.routeHandlerComponent.Response;
import jswf.framework.Environment;

public class AnyMethodHandler implements RequestHandlerInterface {

    public void handle(Environment environment) {
        Request request = (Request) environment.getRequest();
        Response response = (Response) environment.getResponse();
        try {
            response.addContent("method: " + request.getMethod());
        } catch (Exception e) {

        }
    }

}
