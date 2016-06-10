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

    protected String hostname = "localhost";

    protected int port = 8888;

    public Framework(){
        server = new Server();
    }

    public String getHostname() {
        return hostname;
    }

    public Framework setHostname(String hostname) {
        this.hostname = hostname;

        return this;
    }

    public int getPort() {
        return port;
    }

    public Framework setPort(int port) {
        this.port = port;

        return this;
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
        http.setHost(hostname);
        http.setPort(port);
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
        Environment environment = new Environment(new Request(request), new Response(response));

        if (firstComponent != null) {
            firstComponent.invoke(environment);
        }

        baseRequest.setHandled(true);
    }

}
