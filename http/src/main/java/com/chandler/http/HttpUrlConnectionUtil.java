package com.chandler.http;

import android.webkit.URLUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by weijie on 18-3-23.
 */

public class HttpUrlConnectionUtil {
    public static HttpURLConnection execute(Request request) throws AppException {
        if (!URLUtil.isNetworkUrl(request.url)) {
            throw new AppException("the url " + request.url + " is no valid");
        }
        switch (request.method) {
            case GET:
                return get(request);
            case POST:
                return post(request);
            case PUT:
                return get(request);
            case DELETE:
                return get(request);
        }

        return null;
    }


    private static HttpURLConnection get(Request request) throws AppException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(request.url).openConnection();
            connection.setRequestMethod(request.method.name());
            connection.setConnectTimeout(15 * 3000);
            connection.setReadTimeout(15 * 3000);

            addHeader(connection, request.headers);
            return connection;
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(e.getMessage());
        }


    }


    private static HttpURLConnection post(Request request) throws AppException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(request.url).openConnection();
            connection.setRequestMethod(request.method.name());
            connection.setConnectTimeout(15 * 3000);
            connection.setReadTimeout(15 * 3000);
            connection.setDoOutput(true);


            addHeader(connection, request.headers);

            OutputStream os = connection.getOutputStream();
            os.write(request.content.getBytes());
            return connection;
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(e.getMessage());
        }
    }

    private static void addHeader(HttpURLConnection connection, Map<String, String> headers) {
        if (headers == null || headers.size() == 0) {
            return;
        }

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }
}
