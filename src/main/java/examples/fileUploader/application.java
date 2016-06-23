package examples.fileUploader;

import jswf.framework.Framework;
import jswf.commons.components.http.RouteHandlerComponent;
import jswf.commons.components.http.LogRequestComponent;
import jswf.commons.components.http.StaticFilesServerComponent;
import jswf.commons.runners.Http;

import examples.fileUploader.handlers.UploadHandler;

public class application {

    public static void main(String args[]) {
        RouteHandlerComponent routeHandler = new RouteHandlerComponent();
        routeHandler.addGet("upload", "/upload", new UploadHandler());

        Http runner = new Http();

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
        ;

        try {
            framework.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
