package com.chandler.http;

/**
 * Created by yaomenglin on 2018/3/23.
 */

public abstract class StringCallback extends AbstractCallback<String> {
    @Override
    protected String bindData(String result) throws AppException {
        return result;
    }
}
