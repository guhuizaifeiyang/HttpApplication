package com.chandler.httpapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.chandler.http.Callback;
import com.chandler.http.ICallback;
import com.chandler.http.Request;
import com.chandler.http.RequestTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.btn);
        mButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
//                testHttpPostOnSubThread();
                testHttpPostOnSubThreadForGeneric();
        }

    }

    private void testHttpPostOnSubThreadForGeneric() {
        String url = "http://api.stay4it.com/v1/public/core/?service=user.login";
        String content = "account=stay4it&password=123456";
        Request request = new Request(url, Request.RequestMethod.POST);
        request.setCallback(new Callback<User>() {

            @Override
            public void onSuccess(String result) {
                Log.d("chandler", "testHttpGet return:" + result);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        }.setReturnType(User.class));
        request.content = content;
        RequestTask task = new RequestTask(request);
        task.execute();
    }

    private void testHttpPostOnSubThread() {
        String url = "http://api.stay4it.com/v1/public/core/?service=user.login";
        String content = "account=stay4it&password=123456";
        Request request = new Request(url, Request.RequestMethod.POST);
        request.setCallback(new Callback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.d("chandler", "testHttpGet return:" + result);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
        request.content = content;
        RequestTask task = new RequestTask(request);
        task.execute();
    }
}
