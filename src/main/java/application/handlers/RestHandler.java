package application.handlers;

import jswf.commons.components.http.routeHandlerComponent.Request;
import jswf.commons.components.http.routeHandlerComponent.RequestHandlerInterface;
import jswf.commons.components.http.routeHandlerComponent.Response;
import jswf.commons.components.http.routeHandlerComponent.Route;
import jswf.framework.Environment;

import java.io.IOException;

public class RestHandler implements RequestHandlerInterface {

    public void handle(Environment environment) {
        Request request = (Request) environment.getRequest();
        Response response = (Response) environment.getResponse();
        try {
            response.addContent("method: "+request.getMethod());
        } catch (Exception e) {

        }
    }

}
