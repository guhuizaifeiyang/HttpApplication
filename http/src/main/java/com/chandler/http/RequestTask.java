package com.chandler.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;


/**
 * Created by weijie on 18-3-23.
 */

public class RequestTask extends AsyncTask<Void, Integer, Object>{
    public Request request;

    public RequestTask(Request request) {
        this.request = request;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        return request(0);
    }

    public Object request(int retry) {
        try {
            HttpURLConnection connection =  HttpUrlConnectionUtil.execute(request);
            if (request.isEnableProgress()) {
                return request.callback.parse(connection, new OnProgressUpdateListener() {

                    @Override
                    public void onProgressUpdated(int curLen, int totalLen) {
                        publishProgress(curLen, totalLen);
                    }
                });
            } else {
                return request.callback.parse(connection);
            }

        } catch (AppException e) {
            e.printStackTrace();
            if (e.type == AppException.ErrorType.TIMEOUT) {
                if (retry < request.maxRetryCount) {
                    retry++;
                    return request(retry);
                }
            }
            return e;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        request.callback.onProgressUpdated(values[0], values[1]);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o instanceof AppException) {
            if (request.onGlobalExceptionListener != null) {
                if (!request.onGlobalExceptionListener.handleException((AppException) o)) {
                    request.callback.onFailure((AppException) o);
                }
            }
            Log.d("chandler", "onPostExecute: exception = "+(AppException) o);
        } else {
            request.callback.onSuccess((String) o);
            Log.d("chandler", "onPostExecute: result = " + o);
        }
    }
}
