package com.chandler.http;

/**
 * Created by yaomenglin on 2018/3/23.
 */

public abstract class FileCallback extends AbstractCallback<String> {
    @Override
    protected String bindData(String path) throws AppException {
        return path;
    }
}
