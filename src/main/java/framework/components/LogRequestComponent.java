package framework.components;

import framework.AbstractComponent;
import framework.Environment;

import framework.Request;
import framework.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogRequestComponent extends AbstractComponent {

    private static final Logger logger = LoggerFactory.getLogger("LogRequestComponent");

    public void invoke(Environment environment) {
        Request request = environment.getRequest();
        Response response = environment.getResponse();

        long initialTimestamp = System.currentTimeMillis();

        System.out.println(initialTimestamp + " | -> " + request.getMethod() + ": " + request.getRequestURI());

        next(environment);

        int statusCode = response.getStatus();
        long finalTimestamp = System.currentTimeMillis();
        System.out.println(finalTimestamp + " | <- (" + (finalTimestamp - initialTimestamp) + "ms) " + statusCode + " " + HttpStatus.getMessage(response.getStatus()));
    }

}
