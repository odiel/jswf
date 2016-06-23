package application;

import application.handlers.IndexHandler;
import jswf.commons.components.http.RouteHandlerComponent;
import jswf.commons.components.http.LogRequestComponent;
import jswf.commons.components.http.StaticFilesServerComponent;
import jswf.commons.runners.Http;
import jswf.framework.Framework;

public class uploader_application {

    public static void main(String args[]) {
        RouteHandlerComponent routeHandler = new RouteHandlerComponent();
        routeHandler.addGet("index", "/", new IndexHandler());
        //routeHandler.addGet("upload", "/", new IndexHandler());

        Http runner = new Http();

        StaticFilesServerComponent staticFilesServerComponent = new StaticFilesServerComponent();
        staticFilesServerComponent
                .setBasePath(System.getProperty("user.dir"))
                .addAllowedFileExtension("html")
                .addAllowedFileExtension("mp4")
                .addPath("/src/main/java/application/public", "/{(.*)*}")
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
