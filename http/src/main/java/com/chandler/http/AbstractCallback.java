package com.chandler.http;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by yaomenglin on 2018/3/23.
 */

public abstract class AbstractCallback<T> implements ICallback<T> {
    private String path;

    @Override
    public T parse(HttpURLConnection connection) throws AppException {
        return parse(connection, null);
    }

    @Override
    public T parse(HttpURLConnection connection, OnProgressUpdateListener progressUpdateListener) throws AppException {
        try {
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                String result;
                if (path == null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    InputStream is = connection.getInputStream();
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    is.close();
                    out.flush();
                    out.close();
                    result = new String(out.toByteArray());
                    Log.d("chandler", "parse: result =" + result);
                    return bindData(result);
                } else {
                    FileOutputStream out = new FileOutputStream(path);
                    InputStream is = connection.getInputStream();
                    byte[] buffer = new byte[2048];
                    int totalLen = connection.getContentLength();
                    int len, curLen = 0;
                    while ((len = is.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                        curLen += len;
                        if (progressUpdateListener != null) {
                            progressUpdateListener.onProgressUpdated(curLen, totalLen);
                        }
                    }
                    is.close();
                    out.flush();
                    out.close();
                    return bindData(path);
                }
            } else {
                throw new AppException(status, connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(e.getMessage());
        }
    }

    @Override
    public void onProgressUpdated(int curLen, int totalLen) {

    }

    protected abstract T bindData(String result) throws AppException;

    public ICallback setCachePath(String path) {
        this.path = path;
        return this;
    }
}
