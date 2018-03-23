package com.chandler.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;


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
            return HttpUrlConnectionUtil.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
