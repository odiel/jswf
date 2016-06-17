package jswf.commons.components.http.statiFilesServerComponent;

import jswf.commons.components.http.routeHandlerComponent.Route;
import jswf.framework.RequestHandlerInterface;

import java.util.ArrayList;

public class StaticFileRoute extends Route {

    protected String path;

    public StaticFileRoute(ArrayList<String> methods, String path, String uri, RequestHandlerInterface handler) {
        super(methods, path, uri, handler);

        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
