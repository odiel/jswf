package common.runners;

import application.Request;
import application.Response;
import framework.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpRunner extends AbstractHandler implements RunnerInterface {

    protected Server server;

    protected String hostname = "localhost";

    protected int port = 8888;

    protected int idleTimeOut = 30000;

    protected ComponentInterface component;

    protected Environment environment;

    public HttpRunner() {
        server = new Server();
    }

    public String getHostname() {
        return hostname;
    }

    public HttpRunner setHostname(String hostname) {
        this.hostname = hostname;

        return this;
    }

    public int getPort() {
        return port;
    }

    public HttpRunner setPort(int port) {
        this.port = port;

        return this;
    }

    public Server getServer() {
        return server;
    }

    public void run(ComponentInterface component, Environment environment) throws Exception {
        this.component = component;
        this.environment = environment;

        ServerConnector http = new ServerConnector(this.server);
        http.setHost(hostname);
        http.setPort(port);
        http.setIdleTimeout(idleTimeOut);

        server.addConnector(http);
        server.setHandler(this);

        server.start();
        server.join();
    }

    public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        environment
                .setRequest(new Request(request))
                .setResponse(new Response(response))
        ;

        component.invoke(environment);

        baseRequest.setHandled(true);
    }

}
