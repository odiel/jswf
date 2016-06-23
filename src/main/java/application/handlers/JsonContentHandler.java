package application.handlers;

import jswf.commons.components.http.routeHandlerComponent.Request;
import jswf.commons.components.http.routeHandlerComponent.RequestHandlerInterface;
import jswf.commons.components.http.routeHandlerComponent.Response;
import jswf.commons.components.http.routeHandlerComponent.Route;
import jswf.framework.Environment;

import java.io.IOException;

public class JsonContentHandler implements RequestHandlerInterface {

    public void handle(Environment environment) {
        Response response = (Response) environment.getResponse();
        response.setContentType("application/json");
        try {
            Request request = (Request) environment.getRequest();
            Route route = (Route) request.getRoute();
            response.addContent(request.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
