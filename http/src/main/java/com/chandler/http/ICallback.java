package com.chandler.http;

import java.net.HttpURLConnection;

/**
 * Created by weijie on 18-3-23.
 */

public interface ICallback<T> {
    public void onSuccess(String result);

    public void onFailure(Exception o);

    T parse(HttpURLConnection connection) throws Exception;
}
