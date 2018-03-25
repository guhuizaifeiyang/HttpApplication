package com.chandler.httpapplication;

import android.support.v7.app.AppCompatActivity;

import com.chandler.http.AppException;
import com.chandler.http.OnGlobalExceptionListener;
import com.chandler.http.RequestManager;

/**
 * Created by yaomenglin on 2018/3/24.
 */

public class BaseActivity extends AppCompatActivity implements OnGlobalExceptionListener {
    @Override
    public boolean handleException(AppException e) {
        if (e.statusCode == 403) {
            if ("token invalid".equals(e.responseMessage)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        RequestManager.getInstance().cancelRequest(toString());
    }
}
