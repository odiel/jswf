package jswf.framework;

import java.util.HashMap;

public class Environment {

    RequestInterface request;

    ResponseInterface response;

    HashMap<String, Object> customs;

    public Environment() {
        customs = new HashMap<>();
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

}
