package framework;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Response {

    protected HttpServletResponse httpServletResponse;

    public Response(HttpServletResponse response) {
        httpServletResponse = response;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public Response setStatus(int status) {
        httpServletResponse.setStatus(status);

        return this;
    }

    public int getStatus() {
        return httpServletResponse.getStatus();
    }

    public Response setContentType(String contentType) {
        httpServletResponse.setContentType(contentType);

        return this;
    }

    public String getContentType() {
        return httpServletResponse.getContentType();
    }

    public PrintWriter getWriter() throws IOException {
        return httpServletResponse.getWriter();
    }

}
