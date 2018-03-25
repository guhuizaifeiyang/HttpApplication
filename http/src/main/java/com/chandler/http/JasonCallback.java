package com.chandler.http;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;

/**
 * Created by weijie on 18-3-23.
 */

public abstract class JasonCallback<T> extends AbstractCallback<T> {
    @Override
    protected T bindData(String result) throws AppException {
        try {
            JSONObject json = new JSONObject(result);
            Object data = json.opt("data");
            Gson gson = new Gson();
            Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            return gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new AppException(e.getMessage());
        }
    }
}
