package jswf.commons.components.ws;

import jswf.commons.components.http.AbstractRouteBasedComponent;
import jswf.commons.components.http.routeHandlerComponent.Request;
import jswf.commons.components.http.routeHandlerComponent.Response;
import jswf.commons.components.http.routeHandlerComponent.Route;
import jswf.commons.components.ws.webSocketComponent.CustomSocketInterface;
import jswf.commons.components.ws.webSocketComponent.CustomWebSocketServerFactory;
import jswf.framework.Environment;
import org.eclipse.jetty.io.MappedByteBufferPool;
import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.util.ArrayList;

public class WebSocketComponent extends AbstractRouteBasedComponent {

    public WebSocketComponent() {}

    public WebSocketComponent addRoute(String uri, String name, Class<?> handler) {
        ArrayList<String> methods = new ArrayList<>();
        methods.add(Route.METHOD_ANY);

        Route route = new Route(methods, name, uri, handler);
        addRoute(route);

        return this;
    }

    public void invoke(Environment environment) {
        Request request = (Request) environment.getRequest();

        String uri = request.getRequestURI();
        String method = request.getMethod();

        Route route = this.getRouteMatch(method, uri);

        if (route != null) {
            request.setRoute(route);

            try {
                if (!handle(environment, route)) {
                    next(environment);
                }
            } catch (Exception e) {
                if (!(e instanceof FileNotFoundException)) {
                    environment.setException(e);
                }

                next(environment);
            }
        } else {
            next(environment);
        }
    }

    public boolean handle(Environment environment, Route route) throws ServletException, IOException {
        Request request = (Request) environment.getRequest();
        Response response = (Response) environment.getResponse();

        WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.SERVER);
        CustomWebSocketServerFactory webSocketFactory = new CustomWebSocketServerFactory(policy, new MappedByteBufferPool());
        webSocketFactory.setEnvironment(environment);

        Class<?> clazz = route.getHandler();
        if (CustomSocketInterface.class.isAssignableFrom(clazz)) {
            webSocketFactory.register(route.getHandler());
        } else {
            throw new InvalidClassException(clazz.toString() + " must implement jswf.commons.components.ws.webSocketComponent.CustomSocketInterface");
        }

        HttpServletRequest servletRequest = request.getHttpServletRequest();
        HttpServletResponse servletResponse = response.getHttpServletResponse();
        org.eclipse.jetty.server.Request baseRequest = (org.eclipse.jetty.server.Request) environment.getCustom("baseRequest");

        if (webSocketFactory.isUpgradeRequest(servletRequest, servletResponse))
        {
            webSocketFactory.init(baseRequest.getContext());

            // We have an upgrade request
            if (webSocketFactory.acceptWebSocket(servletRequest, servletResponse)) {
                return true;
            }

            // If we reach this point, it means we had an incoming request to upgrade
            // but it was either not a proper websocket upgrade, or it was possibly rejected
            // due to incoming request constraints (controlled by WebSocketCreator)
            if (servletResponse.isCommitted()) {
                // not much we can do at this point.
                return true;
            }
        }

        return false;
    }

}
