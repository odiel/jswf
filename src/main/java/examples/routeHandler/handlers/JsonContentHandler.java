package examples.routeHandler.handlers;

import jswf.commons.components.http.routeHandlerComponent.Request;
import jswf.commons.components.generic.RequestHandlerInterface;
import jswf.commons.components.http.routeHandlerComponent.Response;
import jswf.framework.Environment;

import java.io.IOException;

public class JsonContentHandler implements RequestHandlerInterface {

    public void handle(Environment environment) {
        Response response = (Response) environment.getResponse();
        response.setContentType("examples/json");
        try {
            Request request = (Request) environment.getRequest();
            response.addContent(request.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
