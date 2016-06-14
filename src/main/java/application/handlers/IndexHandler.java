package application.handlers;

import application.Request;
import application.Response;
import common.components.httpRouteHandlerComponent.HttpRoute;
import framework.Environment;
import framework.RequestHandlerInterface;

import java.io.IOException;

public class IndexHandler implements RequestHandlerInterface {

    public void handle(Environment environment) {
        Response response = (Response) environment.getResponse();
        response.setContentType("application/json");
        try {
            Request request = (Request) environment.getRequest();
            HttpRoute route = (HttpRoute) request.getRoute();
            response.addContent("content "+route.getUriParameter("uriParameter1"));
        } catch (IOException e) {

        }
    }

}
