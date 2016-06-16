package framework;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface RouteInterface {

    public String getName();

    public RouteInterface setName(String name);

    public String getUri();

    public RouteInterface setUri(String path);

    public Pattern getCompiledPath();

    public RouteInterface setCompiledPath(Pattern compiledPath);

    public RequestHandlerInterface getHandler();

    public RouteInterface setHandler(RequestHandlerInterface handler);

    public Matcher matcher(String path);

    public boolean matches(String path);

}
