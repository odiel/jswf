package application;

import jswf.framework.Framework;
import jswf.commons.runners.Http;
import jswf.commons.components.http.LogRequestComponent;
import jswf.commons.components.http.RouteHandlerComponent;
import application.handlers.IndexHandler;

public class application {

    public static void main(String args[]) {
        RouteHandlerComponent routeHandler = new RouteHandlerComponent();
        routeHandler.addGet("index", "/", new IndexHandler());
//        //routeHandler.addGet("pepe", "/pepe/{whatever}", new IndexHandler());
        routeHandler.addGet("pepe", "/pepe/{whatever:(kuka)+}/{(something)}/", new IndexHandler());
        routeHandler.addGet("pepe1", "/pepe/", new IndexHandler());

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
