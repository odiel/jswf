package application.handlers;

import common.components.httpRouteHandlerComponent.HttpRequest;
import common.components.httpRouteHandlerComponent.HttpResponse;
import common.components.httpRouteHandlerComponent.HttpRoute;
import framework.Environment;
import framework.RequestHandlerInterface;

import java.io.IOException;

public class IndexHandler implements RequestHandlerInterface {

    public void handle(Environment environment) {
        HttpResponse response = (HttpResponse) environment.getResponse();
        response.setContentType("application/json");
        try {
            HttpRequest request = (HttpRequest) environment.getRequest();
            HttpRoute route = (HttpRoute) request.getRoute();
            response.addContent("content "+route.getUriParameter("uriParameter1"));
        } catch (IOException e) {

        }
    }

}
