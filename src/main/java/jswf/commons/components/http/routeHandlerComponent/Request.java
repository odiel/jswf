package jswf.commons.components.http.routeHandlerComponent;

import jswf.framework.RequestInterface;
import jswf.framework.RouteInterface;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Enumeration;

public class Request implements RequestInterface {

    protected HttpServletRequest httpServletRequest;

    protected RouteInterface route;

    protected boolean isBodyExtracted = false;
    protected String body;

    public Request(HttpServletRequest request) {
        httpServletRequest = request;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public RouteInterface getRoute() {
        return route;
    }

    public Request setRoute(RouteInterface route) {
        this.route = route;

        return this;
    }

    public String getBody() throws IOException {
        if (!isBodyExtracted) {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;

            // TODO: 6/21/2016 Take in consideration character encoding header coming from the client to encode the body properly
            try {
                bufferedReader = httpServletRequest.getReader();
                if (bufferedReader != null) {
                    char[] charBuffer = new char[128];
                    int bytesRead = -1;
                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }
                } else {
                    stringBuilder.append("");
                }

                body = stringBuilder.toString();
                isBodyExtracted = true;
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }
        }

        return body;
    }


    //Exposing the HttpServletRequest methods
    public String getRequestURI() {
        return  httpServletRequest.getRequestURI();
    }

    public String getMethod() {
        return  httpServletRequest.getMethod();
    }

    public String getAuthType() {
        return httpServletRequest.getAuthType();
    }

    public Cookie[] getCookies() {
        return httpServletRequest.getCookies();
    }

    public long getDateHeader(String name) {
        return httpServletRequest.getDateHeader(name);
    }

    public String getHeader(String name) {
        return httpServletRequest.getHeader(name);
    }

    public Enumeration<String> getHeaders(String name){
        return httpServletRequest.getHeaders(name);
    }

    public Enumeration<String> getHeaderNames() {
        return httpServletRequest.getHeaderNames();
    }

    public int getIntHeader(String name) {
        return httpServletRequest.getIntHeader(name);
    }

    public String getPathInfo() {
        return httpServletRequest.getPathInfo();
    }

    public String getPathTranslated() {
        return httpServletRequest.getPathTranslated();
    }

    public String getContextPath() {
        return httpServletRequest.getContextPath();
    }

    public String getQueryString() {
        return httpServletRequest.getQueryString();
    }

    public String getRemoteUser() {
        return httpServletRequest.getRemoteUser();
    }

    public String getRequestedSessionId() {
        return httpServletRequest.getRequestedSessionId();
    }

    public StringBuffer getRequestURL() {
        return httpServletRequest.getRequestURL();
    }

    public String getServletPath() {
        return httpServletRequest.getServletPath();
    }

    public HttpSession getSession(boolean create) {
        return httpServletRequest.getSession(create);
    }

    public HttpSession getSession() {
        return httpServletRequest.getSession();
    }

    public String changeSessionId() {
        return httpServletRequest.changeSessionId();
    }

    public boolean isRequestedSessionIdValid() {
        return httpServletRequest.isRequestedSessionIdValid();
    }

    public boolean isRequestedSessionIdFromCookie() {
        return httpServletRequest.isRequestedSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromURL() {
        return httpServletRequest.isRequestedSessionIdFromURL();
    }

    public Collection<Part> getParts() throws IOException, ServletException {
        return httpServletRequest.getParts();
    }

    public Part getPart(String name) throws IOException, ServletException {
        return httpServletRequest.getPart(name);
    }

    public Object getAttribute(String name) {
        return httpServletRequest.getAttribute(name);
    }

    public Enumeration<String> getAttributeNames() {
        return httpServletRequest.getAttributeNames();
    }

    public boolean isAsyncSupported() {
        return httpServletRequest.isAsyncSupported();
    }

    public boolean isAsyncStarted() {
        return httpServletRequest.isAsyncStarted();
    }

    public AsyncContext startAsync() throws IllegalStateException {
        return httpServletRequest.startAsync();
    }

    public BufferedReader getReader() throws IOException {
        return httpServletRequest.getReader();
    }

    public ServletInputStream getInputStream() throws IOException {
        return httpServletRequest.getInputStream();
    }

}
