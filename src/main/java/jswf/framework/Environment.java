package jswf.framework;

import java.util.HashMap;

public class Environment {

    HashMap<String, Object> services;

    RequestInterface request;

    ResponseInterface response;

    Exception exception;

    HashMap<String, Object> customs;

    public Environment(HashMap<String, Object> services) {
        this.services = services;
        this.customs = new HashMap<>();
    }

    public Environment setRequest(RequestInterface request) {
        this.request = request;

        return this;
    }

    public Environment setResponse(ResponseInterface response) {
        this.response = response;

        return this;
    }

    public RequestInterface getRequest() {
        return request;
    }

    public ResponseInterface getResponse() {
        return response;
    }

    public HashMap<String, Object> getCustoms() {
        return customs;
    }

    public void setCustoms(HashMap<String, Object> customs) {
        this.customs = customs;
    }

    public Object getCustom(String key) {
        return customs.get(key);
    }

    public Environment setCustom(String key, Object object) {
        customs.put(key, object);

        return this;
    }

    public HashMap<String, Object> getServices() { return services; }

    public Object getService(String id) { return services.get(id); }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
