package com.chandler.http;

import java.util.Map;

/**
 * Created by weijie on 18-3-23.
 */

public class Request {
    public enum RequestMethod {GET, POST, PUT, DELETE}

    public ICallback callback;

    public String url;
    public String content;
    public Map<String, String> headers;

    public RequestMethod method;

    public Request(String url,RequestMethod method){
        this.url = url;
        this.method = method;
    }

    public Request(String url){
        this.url = url;
        this.method = RequestMethod.GET;
    }

    public void setCallback(ICallback callback) {
        this.callback = callback;
    }
}
