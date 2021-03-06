package examples.fileUploader;

import jswf.commons.components.http.DummyExceptionRendererComponent;
import jswf.commons.runners.JettyServer;
import jswf.framework.Framework;
import jswf.commons.components.http.RouteHandlerComponent;
import jswf.commons.components.http.LogRequestComponent;
import jswf.commons.components.http.StaticFilesServerComponent;

import examples.fileUploader.handlers.UploadHandler;

public class application {

    public static void main(String args[]) {
        RouteHandlerComponent routeHandler = new RouteHandlerComponent();
        routeHandler.addGet("upload", "/upload", UploadHandler.class);

        JettyServer runner = new JettyServer();
        runner.setPort(8080);

        StaticFilesServerComponent staticFilesServerComponent = new StaticFilesServerComponent();
        staticFilesServerComponent
                .setBasePath(System.getProperty("user.dir"))
                .addPath("/src/main/java/examples/fileUploader/public", "/{(.*)*}")
        ;

        Framework framework = new Framework();
        framework
                .setRunner(runner)
                .addComponent(new LogRequestComponent())
                .addComponent(staticFilesServerComponent)
                .addComponent(routeHandler)
                .addComponent(new DummyExceptionRendererComponent())
        ;

        try {
            framework.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
