package examples.routeHandler.handlers;

import jswf.commons.components.http.routeHandlerComponent.RequestHandlerInterface;
import jswf.commons.components.http.routeHandlerComponent.Response;
import jswf.framework.Environment;

import java.io.IOException;

public class IndexHandler implements RequestHandlerInterface {

    public void handle(Environment environment) {
        Response response = (Response) environment.getResponse();

        try {
            response.addContent("Hello World!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
