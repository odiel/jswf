package application;

import framework.Framework;
import common.runners.HttpRunner;
import common.components.LogRequestComponent;
import common.components.HttpRouteHandlerComponent;
import application.handlers.IndexHandler;

public class application {

    public static void main(String args[]) {
        HttpRouteHandlerComponent routeHandler = new HttpRouteHandlerComponent();
        routeHandler.addGet("index", "/", new IndexHandler());
        //routeHandler.addGet("pepe", "/pepe/{whatever}", new IndexHandler());
        routeHandler.addGet("pepe", "/pepe/{whatever:(kuka)+}/{(something)}/", new IndexHandler());

        HttpRunner runner = new HttpRunner();

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
