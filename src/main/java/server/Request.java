package server;

public class Request {
    private String method;
    private String path;
    private String header;
    private String body;




    public Request() {

    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "server.Request{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", header='" + header + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
