package jswf.commons.components.http;

import jswf.commons.components.http.routeHandlerComponent.Request;
import jswf.commons.components.http.routeHandlerComponent.Route;
import jswf.commons.components.http.statiFilesServerComponent.StaticFileHandler;
import jswf.commons.components.http.statiFilesServerComponent.StaticFileRoute;
import jswf.framework.Environment;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.util.URIUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InvalidClassException;
import java.util.ArrayList;

public class StaticFilesServerComponent extends AbstractRouteBasedComponent {

    private String basePath;

    private ArrayList<String> allowedFileExtensions;

    private MimeTypes mimeTypes;

    public StaticFilesServerComponent() {
        super();

        allowedFileExtensions = new ArrayList<>();

        allowedFileExtensions.add("html");
        allowedFileExtensions.add("css");
        allowedFileExtensions.add("js");

        allowedFileExtensions.add("jpeg");
        allowedFileExtensions.add("jpg");
        allowedFileExtensions.add("png");
        allowedFileExtensions.add("gif");

        allowedFileExtensions.add("otf");
        allowedFileExtensions.add("eot");
        allowedFileExtensions.add("svg");
        allowedFileExtensions.add("ttf");
        allowedFileExtensions.add("woff");
        allowedFileExtensions.add("woff2");

        mimeTypes = new MimeTypes();
    }

    public String getBasePath() {
        return basePath;
    }

    public StaticFilesServerComponent setBasePath(String basePath) {
        this.basePath = basePath;

        return this;
    }

    public StaticFilesServerComponent addPath(String path, String uri) {
        ArrayList<String> methods = new ArrayList<>();
        methods.add(Route.METHOD_GET);
        methods.add(Route.METHOD_HEAD);

        addRoute(new StaticFileRoute(methods, path, uri, StaticFileHandler.class));

        return this;
    }

    public StaticFilesServerComponent addAllowedFileExtension(String fileExtension) {
        allowedFileExtensions.add(fileExtension);

        return this;
    }

    public void invoke(Environment environment) {
        Request request = (Request) environment.getRequest();

        String uri = request.getRequestURI();
        String method = request.getMethod();

        StaticFileRoute route = (StaticFileRoute) this.getRouteMatch(method, uri);

        if (route != null) {
            request.setRoute(route);

            try {
                String path = URIUtil.addPaths(basePath, route.getPath());
                path = URIUtil.addPaths(path, request.getRequestURI());

                File file = new File(path);

                if (!file.exists()) {
                    throw new FileNotFoundException("Static file [" + path + "] does not exists.");
                }

                String fileName = file.getName();
                String fileExtension = fileName.substring(fileName.lastIndexOf('.')+1);

                if (allowedFileExtensions.size() > 0 && !allowedFileExtensions.contains(fileExtension)) {
                    throw new FileNotFoundException("Static file [" + path + "] was found but is not allowed by the extension [" + fileExtension + "].");
                }

                Class<?> clazz = route.getHandler();
                Object instance = clazz.newInstance();
                if (instance instanceof StaticFileHandler) {
                    StaticFileHandler handler = (StaticFileHandler) instance;
                    environment.setCustom("staticFileServer.file", file);
                    environment.setCustom("staticFileServer.mimeTypes", mimeTypes);
                    handler.handle(environment);
                } else {
                    throw new InvalidClassException(clazz.toString() + " must implement jswf.commons.components.http.statiFilesServerComponent.StaticFileHandler");
                }
            } catch (Exception e) {
                if (!(e instanceof FileNotFoundException)) {
                    environment.setException(e);
                }

                next(environment);
            }
        } else {
            next(environment);
        }
    }

}
