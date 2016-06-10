package handlers;

import framework.Environment;
import framework.RequestHandlerInterface;
import framework.Response;

import java.io.IOException;

public class IndexHandler implements RequestHandlerInterface {

    public void handle(Environment environment) {
        Response response = environment.getResponse();
        response.setContentType("application/json");
        try {
            response.getWriter().append("content");
        } catch (IOException e) {

        }
    }

}
