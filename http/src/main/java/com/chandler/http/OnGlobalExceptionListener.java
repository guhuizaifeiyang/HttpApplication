package com.chandler.http;

import com.chandler.http.AppException;

/**
 * Created by yaomenglin on 2018/3/24.
 */

public interface OnGlobalExceptionListener {
    boolean handleException(AppException e);
}
