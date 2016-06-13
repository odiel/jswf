package framework;

public class Environment {

    Request request;

    Response response;

    public Environment() {}

    public Environment setRequest(Request request) {
        this.request = request;

        return this;
    }

    public Environment setResponse(Response response) {
        this.response = response;

        return this;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}
