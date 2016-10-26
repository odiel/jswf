package jswf.commons.components.http;

import jswf.framework.AbstractComponent;
import jswf.framework.Environment;
import jswf.commons.components.http.routeHandlerComponent.Request;
import jswf.commons.components.http.routeHandlerComponent.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class LogRequestComponent extends AbstractComponent {

    private static final Logger logger = LoggerFactory.getLogger("LogRequestComponent");

    public void invoke(Environment environment) {
        Request request = (Request) environment.getRequest();
        Response response = (Response) environment.getResponse();

        long initialTimestamp = System.currentTimeMillis();

        String protocol = "";

        try {
            URL url = new URL(request.getRequestURL().toString());
            protocol = url.getProtocol();
        } catch (Exception e) {}

        System.out.println(initialTimestamp + " | -> " + request.getRequestURI() + " | " + protocol.toUpperCase() + " | " + request.getMethod());

        next(environment);

        int statusCode = response.getStatus();
        long finalTimestamp = System.currentTimeMillis();
        System.out.println(finalTimestamp + " | <- " + request.getRequestURI() + " | " + (finalTimestamp - initialTimestamp) + "ms | " +  statusCode + " " + HttpStatus.getMessage(response.getStatus()));
    }

}
