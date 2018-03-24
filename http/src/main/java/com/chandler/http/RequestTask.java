package com.chandler.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;


/**
 * Created by weijie on 18-3-23.
 */

public class RequestTask extends AsyncTask<Void, Integer, Object>{
    private Request request;

    public RequestTask(Request request) {
        this.request = request;
    }

    @Override
    protected Object doInBackground(Void... voids) {
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

        } catch (IOException e) {
            e.printStackTrace();
            return e;
        } catch (Exception e) {
            e.printStackTrace();
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
        if (o instanceof Exception) {
            Log.d("chandler", "onPostExecute: exception = "+(Exception) o);
        } else {
            Log.d("chandler", "onPostExecute: result = " + o);
        }
    }
}
