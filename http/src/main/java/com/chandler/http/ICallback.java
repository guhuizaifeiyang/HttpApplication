package com.chandler.http;

import java.net.HttpURLConnection;

/**
 * Created by weijie on 18-3-23.
 */

public interface ICallback<T> {
    void onSuccess(String result);

    void onFailure(AppException o);

    T parse(HttpURLConnection connection, OnProgressUpdateListener progressUpdateListener) throws AppException;
    T parse(HttpURLConnection connection) throws AppException;

    void onProgressUpdated(int curLen, int totalLen);

}
