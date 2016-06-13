package framework;

public class Environment {

    RequestInterface request;

    ResponseInterface response;

    public Environment() {}

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
}
