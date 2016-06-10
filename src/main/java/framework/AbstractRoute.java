package framework;

import java.util.regex.Pattern;

public abstract class AbstractRoute implements RouteInterface {

    protected String name;

    protected String path;

    protected Pattern compiledPath;

    protected RequestHandlerInterface handler;

    public AbstractRoute setName(String name) {
        this.name = name;

        return this;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public AbstractRoute setPath(String path) {
        this.path = path;

        return this;
    }

    public Pattern getCompiledPath() {
        return compiledPath;
    }

    public AbstractRoute setCompiledPath(Pattern compiledPath) {
        this.compiledPath = compiledPath;

        return this;
    }

    public RequestHandlerInterface getHandler() {
        return handler;
    }

    public AbstractRoute setHandler(RequestHandlerInterface handler) {
        this.handler = handler;

        return this;
    }

}
