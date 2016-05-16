package framework.components;

import framework.AbstractComponent;
import framework.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class RouteHandlerComponent extends AbstractComponent {

    private static final Logger logger = LoggerFactory.getLogger("LogRequestComponent");

    public void invoke(Environment environment) {
        HttpServletRequest request = environment.getRequest();

        logger.info(request.getMethod() + ": " + request.getRequestURI());

        next(environment);
    }

}
