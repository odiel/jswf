import framework.Framework;
import framework.components.FirstComponent;
import framework.components.LogRequestComponent;
import framework.components.HttpRouteHandlerComponent;
import framework.components.SecondComponent;
import handlers.IndexHandler;

public class application {

    public static void main(String args[]) {
        HttpRouteHandlerComponent routeHandler = new HttpRouteHandlerComponent();
        routeHandler.addGet("index", "/", new IndexHandler());
        //routeHandler.addGet("pepe", "/pepe/{whatever}", new IndexHandler());
        routeHandler.addGet("pepe", "/pepe/{whatever:(kuka)+}/{(something)}/", new IndexHandler());

        Framework framework = new Framework();
        framework
                .addComponent(new LogRequestComponent())
                .addComponent(routeHandler)
                .addComponent(new FirstComponent())
                .addComponent(new SecondComponent())
        ;

        try {
            framework.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
