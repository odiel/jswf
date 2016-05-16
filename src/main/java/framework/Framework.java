package framework;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Framework extends AbstractHandler {

    protected AbstractComponent firstComponent;

    protected AbstractComponent currentComponent;

    protected Server server;

    public Framework(){
        server = new Server();
    }

    public Framework addComponent(AbstractComponent component) {
        if (firstComponent == null) {
            firstComponent = component;
            currentComponent = component;
        } else {
            currentComponent.setNext(component);
            currentComponent = component;
        }

        return this;
    }

    public void run() throws Exception {
        ServerConnector http = new ServerConnector(this.server);
        http.setHost("localhost");
        http.setPort(8888);
        http.setIdleTimeout(30000);

        server.addConnector(http);
        server.setHandler(this);

        server.start();
        server.join();
    }

    public Server getServer() {
        return server;
    }

    public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Environment environment = new Environment(request, response);

        if (firstComponent != null) {
            firstComponent.invoke(environment);
        }

        baseRequest.setHandled(true);
    }

}
