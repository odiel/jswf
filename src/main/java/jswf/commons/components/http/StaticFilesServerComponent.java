package jswf.commons.components.http;

import jswf.framework.Environment;
import jswf.commons.components.http.routeHandlerComponent.Route;
import jswf.commons.components.http.routeHandlerComponent.Request;
import jswf.commons.components.http.routeHandlerComponent.Response;
import jswf.commons.components.http.statiFilesServerComponent.StaticFileRoute;
import jswf.commons.components.http.routeHandlerComponent.RequestHandlerInterface;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.io.WriterOutputStream;
import org.eclipse.jetty.server.HttpOutput;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.URIUtil;

import javax.servlet.AsyncContext;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TODO: 6/14/16 Implement a cache system to cache file content in memory
// TODO: 7/28/2016 Implement header for mime type depending on the file extension, maybe use Jetty mimetype object to identify the mime type

public class StaticFilesServerComponent extends RouteHandlerComponent implements RequestHandlerInterface {

    Environment environment;

    String basePath;

    int minAsyncContentLength = 0;

    int minMemoryMappedContentLength = 1024;

    ArrayList<String> allowedFileExtensions;

    public StaticFilesServerComponent() {
        routes = new ArrayList<Route>();
        allowedFileExtensions = new ArrayList<String>();
        initializedRoutes = new HashMap<String, List<Route>>();
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

        Route route = new StaticFileRoute(methods, path, uri, this);
        addRoute(route);

        return this;
    }

    public StaticFilesServerComponent addAllowedFileExtension(String fileExtension) {
        allowedFileExtensions.add(fileExtension);

        return this;
    }

    public void invoke(Environment environment) {
        this.environment = environment;

        Request request = (Request) environment.getRequest();
        Response response = (Response) environment.getResponse();

        String uri = request.getRequestURI();
        String method = request.getMethod();

        Route route = this.getRouteMatch(method, uri);

        if (route != null) {
            request.setRoute(route);

            try {
                route.getHandler().handle(environment);
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

    public void handle(Environment environment) throws Exception {
        Request request = (Request) environment.getRequest();
        Response response = (Response) environment.getResponse();

        StaticFileRoute route = (StaticFileRoute) request.getRoute();

        String path = URIUtil.addPaths(basePath, route.getPath());
        path = URIUtil.addPaths(path, request.getRequestURI());

        File file = new File(path);

        if (!file.exists()) {
            throw new FileNotFoundException("Static file [" + path + "] does not exists.");
        }

        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.')+1);

        if (allowedFileExtensions.size() > 0) {
            if (!allowedFileExtensions.contains(fileExtension)) {
                throw new FileNotFoundException("Static file [" + path + "] was found but is not allowed by the extension [" + fileExtension + "].");
            }
        }

        String lastModified = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.of("GMT")));
        response.addHeader(HttpHeader.LAST_MODIFIED.toString(), lastModified);

        OutputStream outputStream;

        try {
            outputStream = response.getOutputStream();
        } catch (IllegalStateException e) {
            outputStream = new WriterOutputStream(response.getWriter());
        }

        FileInputStream fileInputStream = new FileInputStream(file);

        // Has the output been wrapped
        if (!(outputStream instanceof HttpOutput)) {
            // Write content via wrapped output
            try (InputStream inputStream = fileInputStream)
            {
                inputStream.skip(0);
                if (file.length() < 0) {
                    IO.copy(inputStream, outputStream);
                } else {
                    IO.copy(inputStream, outputStream, file.length());
                }
            }
        } else {
            HttpOutput httpOutputStream = (HttpOutput) outputStream;

            // select async by size
            int minAsyncSize = minAsyncContentLength == 0 ? response.getBufferSize() : minAsyncContentLength;

            if (request.isAsyncSupported() && minAsyncSize > 0 && file.length() >= minAsyncSize) {
                final AsyncContext async = request.startAsync();
                async.setTimeout(0);

                Callback callback = new Callback()
                {
                    @Override
                    public void succeeded()
                    {
                        async.complete();
                    }

                    @Override
                    public void failed(Throwable x)
                    {
                        async.complete();
                    }
                };

                // Can we use a memory mapped file?
                if (minMemoryMappedContentLength > 0 && file.length() > minMemoryMappedContentLength && file.length() < Integer.MAX_VALUE) {
                    ByteBuffer buffer = BufferUtil.toMappedBuffer(file);
                    httpOutputStream.sendContent(buffer,callback);
                } else {
                    // Do a blocking write of a channel (if available) or input stream
                    // Close of the channel/input stream is done by the async sendContent
                    ReadableByteChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
                    if (channel != null) {
                        httpOutputStream.sendContent(channel, callback);
                    } else {
                        httpOutputStream.sendContent(fileInputStream, callback);
                    }
                }
            } else {
                // Can we use a memory mapped file?
                if (minMemoryMappedContentLength > 0 && file.length() > minMemoryMappedContentLength) {
                    ByteBuffer buffer = BufferUtil.toMappedBuffer(file);
                    httpOutputStream.sendContent(buffer);
                } else {
                    // Do a blocking write of a channel (if available) or input stream
                    ReadableByteChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
                    if (channel != null) {
                        httpOutputStream.sendContent(channel);
                    } else {
                        httpOutputStream.sendContent(fileInputStream);
                    }
                }
            }
        }

    }

}
