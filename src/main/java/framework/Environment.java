package framework;

public class Environment {

    Request request;

    Response response;

    public Environment(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}
