package com.chandler.http;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;

/**
 * Created by yaomenglin on 2018/3/23.
 */

public abstract class AbstractCallback<T> implements ICallback<T> {
    @Override
    public T parse(HttpURLConnection connection) throws Exception {
        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
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
            String result = new String(out.toByteArray());
            Log.d("chandler", "parse: result =" + result);
            return bindData(result);
        }
        return null;
    }

    protected abstract T bindData(String result) throws Exception;
}
