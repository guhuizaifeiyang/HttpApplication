package com.chandler.http;

import java.net.HttpURLConnection;

/**
 * Created by weijie on 18-3-23.
 */

public interface ICallback<T> {
    void onSuccess(String result);

    void onFailure(Exception o);

    T parse(HttpURLConnection connection, OnProgressUpdateListener progressUpdateListener) throws Exception;
    T parse(HttpURLConnection connection) throws Exception;

    void onProgressUpdated(int curLen, int totalLen);

}
