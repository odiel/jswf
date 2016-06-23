package application;

import application.handlers.JsonContentHandler;
import application.handlers.RestHandler;
import jswf.framework.Framework;
import jswf.commons.runners.Http;
import jswf.commons.components.http.LogRequestComponent;
import jswf.commons.components.http.RouteHandlerComponent;
import application.handlers.IndexHandler;

public class application {

    public static void main(String args[]) {
        RouteHandlerComponent routeHandler = new RouteHandlerComponent();
        routeHandler.addGet("index", "/", new IndexHandler());
        routeHandler.addGet("pepe", "/pepe/{whatever:(kuka)+}/{(something)}/", new IndexHandler());
        routeHandler.addPost("json-content", "/json/", new JsonContentHandler());
        routeHandler.addAny("any", "/rest/", new RestHandler());

        Http runner = new Http();

        Framework framework = new Framework();
        framework
                .setRunner(runner)
                .addComponent(new LogRequestComponent())
                .addComponent(routeHandler)
        ;

        try {
            framework.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
