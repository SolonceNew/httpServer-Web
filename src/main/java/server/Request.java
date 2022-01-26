package server;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String method;
    private String path;
    private String header;
    private String body;
    private Map<String, String> queryParamPair = new HashMap<>();

    public String getMethod() {
        return method;
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

    public void setMethod(String method) {
        this.method = method;
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

    public Map<String, String> getQueryParamPair() {
        return queryParamPair;
    }

    public void setQueryParamPair(Map<String, String> queryParamPair) {
        this.queryParamPair = queryParamPair;
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
