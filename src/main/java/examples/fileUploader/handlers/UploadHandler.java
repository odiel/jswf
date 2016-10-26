package examples.fileUploader.handlers;

import jswf.commons.components.http.routeHandlerComponent.Route;
import jswf.commons.components.http.routeHandlerComponent.Response;
import jswf.commons.components.http.routeHandlerComponent.Request;
import jswf.framework.Environment;
import jswf.commons.components.generic.RequestHandlerInterface;

import java.io.IOException;

public class UploadHandler implements RequestHandlerInterface {

    public void handle(Environment environment) {
        Response response = (Response) environment.getResponse();
        response.setContentType("examples/json");
        try {
            Request request = (Request) environment.getRequest();
            Route route = (Route) request.getRoute();
            response.addContent("content "+route.getUriParameter("uriParameter1"));
        } catch (IOException e) {

        }
    }

}
