package jswf.commons.components.http;

import jswf.framework.AbstractComponent;
import jswf.framework.Environment;
import jswf.commons.components.http.routeHandlerComponent.Request;
import jswf.commons.components.http.routeHandlerComponent.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogRequestComponent extends AbstractComponent {

    private static final Logger logger = LoggerFactory.getLogger("LogRequestComponent");

    public void invoke(Environment environment) {
        Request request = (Request) environment.getRequest();
        Response response = (Response) environment.getResponse();

        long initialTimestamp = System.currentTimeMillis();

        System.out.println(initialTimestamp + " | -> " + request.getMethod() + ": " + request.getRequestURI());

        next(environment);

        int statusCode = response.getStatus();
        long finalTimestamp = System.currentTimeMillis();
        System.out.println(finalTimestamp + " | <- (" + (finalTimestamp - initialTimestamp) + "ms) " + statusCode + " " + HttpStatus.getMessage(response.getStatus()));
    }

}
