package examples.websocket;

import jswf.commons.components.http.LogRequestComponent;
import jswf.commons.components.http.StaticFilesServerComponent;
import jswf.commons.components.ws.WebSocketComponent;
import jswf.commons.runners.JettyServer;
import jswf.framework.Framework;

public class application {

    public static void main(String args[]) {
        StaticFilesServerComponent staticFilesServerComponent = new StaticFilesServerComponent();
        staticFilesServerComponent
                .setBasePath(System.getProperty("user.dir"))
                .addPath("/src/main/java/examples/websocket/public", "/{(.*)*}")
        ;

        WebSocketComponent websocketComponent = new WebSocketComponent();
        websocketComponent.addRoute("/echo", "ws_echo", MySocket.class);

        JettyServer runner = new JettyServer();

        Framework framework = new Framework();
        framework
                .setRunner(runner)
                .addComponent(new LogRequestComponent())
                .addComponent(websocketComponent)
                .addComponent(staticFilesServerComponent)
        ;

        try {
            framework.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
