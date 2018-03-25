package com.chandler.http;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;

/**
 * Created by yaomenglin on 2018/3/23.
 */

public abstract class AbstractCallback<T> implements ICallback<T> {
    private String path;
    private volatile boolean isCancelled;

    @Override
    public T parse(HttpURLConnection connection) throws AppException {
        return parse(connection, null);
    }

    @Override
    public T parse(HttpURLConnection connection, OnProgressUpdateListener progressUpdateListener) throws AppException {
        try {
            checkIfCancelled();
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                String result;
                if (path == null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    InputStream is = connection.getInputStream();
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        checkIfCancelled();
                        out.write(buffer, 0, len);
                    }
                    is.close();
                    out.flush();
                    out.close();
                    result = new String(out.toByteArray());
                    Log.d("chandler", "parse: result =" + result);
                    T t = bindData(result);
                    return postRequest(t);
                } else {
                    FileOutputStream out = new FileOutputStream(path);
                    InputStream is = connection.getInputStream();
                    byte[] buffer = new byte[2048];
                    int totalLen = connection.getContentLength();
                    int len, curLen = 0;
                    while ((len = is.read(buffer)) != -1) {
                        checkIfCancelled();
                        out.write(buffer, 0, len);
                        curLen += len;
                        if (progressUpdateListener != null) {
                            progressUpdateListener.onProgressUpdated(curLen, totalLen);
                        }
                    }
                    is.close();
                    out.flush();
                    out.close();
                    T t = bindData(path);
                    return postRequest(t);
                }
            } else {
                throw new AppException(status, connection.getResponseMessage());
            }
        } catch (InterruptedIOException e) {
            e.printStackTrace();
            throw new AppException(AppException.ErrorType.SERVER, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(e.getMessage());
        }
    }

    private void checkIfCancelled() throws AppException{
        if (isCancelled) {
            throw new AppException(AppException.ErrorType.CANCEL, "the request has been cancelled");
        }
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    @Override
    public T preRequest() {
        return null;
    }

    @Override
    public T postRequest(T t) {
        return t;
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
