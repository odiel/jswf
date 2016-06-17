package jswf.commons.components.http;

import jswf.commons.components.http.routeHandlerComponent.Route;
import jswf.commons.components.http.routeHandlerComponent.Request;
import jswf.commons.components.http.routeHandlerComponent.Response;
import jswf.commons.components.http.statiFilesServerComponent.StaticFileRoute;
import jswf.framework.Environment;
import jswf.framework.RequestHandlerInterface;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StaticFilesServerComponent extends RouteHandlerComponent implements RequestHandlerInterface {

    Environment environment;

    String basePath;

    public StaticFilesServerComponent() {
        routes = new ArrayList<Route>();
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

    // TODO: 6/14/16 Cache static files in memory

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
                route.getHandler().handle(this.environment);
            } catch (Exception e) {
                if (e instanceof FileNotFoundException) {
                    next(environment);
                } else {
                    e.printStackTrace();
                }
            }
        } else {
            next(environment);
        }
    }

    public void handle(Environment environment) throws Exception {
        Request request = (Request) environment.getRequest();
        Response response = (Response) environment.getResponse();

        String servletPath;
        String pathInfo;

        StaticFileRoute route = (StaticFileRoute) request.getRoute();

        String path = URIUtil.addPaths(basePath, route.getPath());
        path = URIUtil.addPaths(path, request.getRequestURI());

        File file = new File(path);

        if (!file.exists()) {
            throw new FileNotFoundException("Static file [" + path + "] does not exists.");
        }

        OutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IllegalStateException e) {
            out = new WriterOutputStream(response.getWriter());
        }

        FileInputStream fileInputStream = new FileInputStream(file);
        int _minMemoryMappedContentLength = 1024;

        // Has the output been wrapped
        if (!(out instanceof HttpOutput)) {
            // Write content via wrapped output
            try (InputStream in = fileInputStream)
            {
                in.skip(0);
                if (file.length() < 0)
                    IO.copy(in,out);
                else
                    IO.copy(in,out, file.length());
            }
        } else {
            int _minAsyncContentLength = 0;

            // select async by size
            int min_async_size = _minAsyncContentLength == 0 ? response.getBufferSize() : _minAsyncContentLength;

            if (request.isAsyncSupported() && min_async_size>0 && file.length() >= min_async_size) {
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

//                    // Can we use a memory mapped file?
//                    if (_minMemoryMappedContentLength>0 &&
//                            resource.length()>_minMemoryMappedContentLength &&
//                            resource.length()<Integer.MAX_VALUE &&
//                            resource instanceof PathResource)
//                    {
//                        ByteBuffer buffer = BufferUtil.toMappedBuffer(resource.getFile());
//                        ((HttpOutput)out).sendContent(buffer,callback);
//                    }
//                    else  // Do a blocking write of a channel (if available) or input stream
//                    {
//                        // Close of the channel/inputstream is done by the async sendContent
//                        ReadableByteChannel channel= resource.getReadableByteChannel();
//                        if (channel!=null)
//                            ((HttpOutput)out).sendContent(channel,callback);
//                        else
//                            ((HttpOutput)out).sendContent(resource.getInputStream(),callback);
//                    }
            } else {
                // Can we use a memory mapped file?
                if (_minMemoryMappedContentLength > 0 && file.length() > _minMemoryMappedContentLength) {
                    ByteBuffer buffer = BufferUtil.toMappedBuffer(file);
                    ((HttpOutput)out).sendContent(buffer);
                } else {  // Do a blocking write of a channel (if available) or input stream
                    ReadableByteChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
                    if (channel!=null)
                        ((HttpOutput)out).sendContent(channel);
                    else
                        ((HttpOutput)out).sendContent(fileInputStream);
                }
            }
        }

    }

}
