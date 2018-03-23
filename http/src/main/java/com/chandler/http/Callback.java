package com.chandler.http;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by weijie on 18-3-23.
 */

public abstract class Callback<T> implements ICallback<T> {

    private Class<T> clz;
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
            JSONObject json = new JSONObject(result);
            JSONObject data = json.optJSONObject("data");
            Gson gson = new Gson();
            return gson.fromJson(data.toString(), clz);
        }
        return null;
    }

   public ICallback setReturnType(Class<T> clz) {
       this.clz = clz;
       return this;
   }
}
