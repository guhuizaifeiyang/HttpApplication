package com.chandler.http;

import android.os.Build;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by weijie on 18-3-23.
 */

public class Request {
    private RequestTask task;

    public void checkIfCancelled() throws AppException{
        if (isCancelled) {
            throw new AppException(AppException.ErrorType.CANCEL, "the request has been cancelled");
        }
    }

    public void cancel(boolean force) {
        isCancelled = true;
        callback.cancel();
        if (force && task != null) {
            task.cancel(force);
        }
    }

    public void execute(ExecutorService mRequestExecutor) {
        task = new RequestTask(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(mRequestExecutor);
        } else {
            task.execute();
        }
    }

    public enum RequestMethod {GET, POST, PUT, DELETE}

    public ICallback callback;

    public boolean isEnableProgress() {
        return enableProgress;
    }

    public void setEnableProgress(boolean enableProgress) {
        this.enableProgress = enableProgress;
    }

    public boolean enableProgress= false;
    public String url;
    public String content;
    public Map<String, String> headers;

    public RequestMethod method;

    public final int maxRetryCount = 3;
    public volatile boolean isCancelled = false;

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String tag;

    public void setOnGlobalExceptionListener(OnGlobalExceptionListener onGlobalExceptionListener) {
        this.onGlobalExceptionListener = onGlobalExceptionListener;
    }

    public OnGlobalExceptionListener onGlobalExceptionListener;

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
