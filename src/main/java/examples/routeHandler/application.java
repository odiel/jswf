package examples.routeHandler;

import jswf.framework.Framework;
import jswf.commons.runners.JettyServer;
import jswf.commons.components.http.LogRequestComponent;
import jswf.commons.components.http.RouteHandlerComponent;

import examples.routeHandler.handlers.IndexHandler;
import examples.routeHandler.handlers.JsonContentHandler;
import examples.routeHandler.handlers.AnyMethodHandler;

public class application {

    public static void main(String args[]) {
        RouteHandlerComponent routeHandler = new RouteHandlerComponent();
        routeHandler.addGet("index", "/", IndexHandler.class);
        routeHandler.addPost("json-content", "/json", JsonContentHandler.class);
        routeHandler.addAny("any", "/methods", AnyMethodHandler.class);
        //routeHandler.addGet("pepe", "/pepe/{whatever:(kuka)+}/{(something)}/", new IndexHandler());

        JettyServer runner = new JettyServer();

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
