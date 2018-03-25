package com.chandler.http;

import com.google.gson.stream.JsonReader;

/**
 * Created by yaomenglin on 2018/3/25.
 */

public interface IEntity {
    void readFromJson(JsonReader reader) throws  AppException;
}
