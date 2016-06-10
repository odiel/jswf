package framework;

import javax.servlet.http.HttpServletRequest;

public class Request {

    protected HttpServletRequest httpServletRequest;

    protected RouteInterface route;

    public Request(HttpServletRequest request) {
        httpServletRequest = request;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public String getRequestURI() {
        return  httpServletRequest.getRequestURI();
    }

    public String getMethod() {
        return  httpServletRequest.getMethod();
    }

    public RouteInterface getRoute() {
        return route;
    }

    public Request setRoute(RouteInterface route) {
        this.route = route;

        return this;
    }
}
