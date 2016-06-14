package common.components;

import application.Request;
import application.Response;
import framework.*;
import common.components.httpRouteHandlerComponent.HttpRoute;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.http.HttpStatus;

import java.util.*;

public class HttpRouteHandlerComponent extends AbstractComponent {

    protected List<HttpRoute> routes;
    protected HashMap<String, List<HttpRoute>> initializedRoutes;

    Environment environment;

    public HttpRouteHandlerComponent() {
        routes = new ArrayList<HttpRoute>();
        initializedRoutes = new HashMap<String, List<HttpRoute>>();
    }

    public void invoke(Environment environment) {
        this.environment = environment;

        Request request = (Request) environment.getRequest();
        Response response = (Response) environment.getResponse();

        String uri = request.getRequestURI();
        if (!uri.endsWith("/")) {
            uri += "/";
        }

        String method = request.getMethod();

        HttpRoute route = this.getRouteMatch(method, uri);

        if (route != null) {
            request.setRoute(route);
            route.getHandler().handle(this.environment);
        } else {
            response.setStatus(HttpStatus.NOT_FOUND_404);
            try {
                response.getWriter().print("404, route not found.");
            } catch (Exception e) {}
        }

        next(environment);
    }

    public void addGet(String path, RequestHandlerInterface handler) {
        ArrayList<String> methods = new ArrayList<String>();
        methods.add(HttpRoute.METHOD_GET);

        HttpRoute route = new HttpRoute(methods, DigestUtils.md5Hex(path), path, handler);
        addRoute(route);
    }

    public void addGet(String name, String path, RequestHandlerInterface handler) {
        ArrayList<String> methods = new ArrayList<String>();
        methods.add(HttpRoute.METHOD_GET);

        HttpRoute route = new HttpRoute(methods, name, path, handler);
        addRoute(route);
    }

    public void addRoute(HttpRoute route) {
        routes.add(route);
    }

    protected HttpRoute getRouteMatch(String method, String uri) {
        if (!initializedRoutes.isEmpty()) {
            List<HttpRoute> routesForMethod = initializedRoutes.get(method);
            if (routesForMethod != null && !routesForMethod.isEmpty()) {
                for (HttpRoute route: routesForMethod) {
                    if (route.matches(uri)) {
                        return route;
                    }
                }
            }
        }

        for (HttpRoute route: routes) {
            if (route.getMethods().contains(method) && route.matches(uri)) {
                List<HttpRoute> routesForMethod = initializedRoutes.get(method);
                if (routesForMethod == null) {
                    routesForMethod = new ArrayList<HttpRoute>();
                    initializedRoutes.put(method, routesForMethod);
                }

                routesForMethod.add(route);

                return route;
            }
        }

        return null;
    }

}
