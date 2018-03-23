package com.chandler.http;

/**
 * Created by weijie on 18-3-23.
 */

public interface ICallback {
    public void onSuccess(String result);

    public void onFailure(Exception o);
}
