package com.chandler.httpapplication;

/**
 * Created by yaomenglin on 2018/3/25.
 */

public class Module {
    public String name;
    public long timestamp;

    @Override
    public String toString() {
        return "name: " + name + ", timestamp: " + timestamp;
    }
}
