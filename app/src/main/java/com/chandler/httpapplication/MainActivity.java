package com.chandler.httpapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chandler.http.FileCallback;
import com.chandler.http.ICallback;
import com.chandler.http.JasonCallback;
import com.chandler.http.Request;
import com.chandler.http.RequestTask;

import java.io.File;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_PERMISSIONS = 123;
    private static final String TAG = "chandler";
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
//                testHttpPostOnSubThreadForGeneric();
                testHttpPostForDownload();
        }

    }

    private void testHttpPostOnSubThreadForGeneric() {
        String url = "http://api.stay4it.com/v1/public/core/?service=user.login";
        String content = "account=stay4it&password=123456";
        Request request = new Request(url, Request.RequestMethod.POST);
        request.setCallback(new JasonCallback<User>() {

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

    private void testHttpPostOnSubThread() {
        String url = "http://api.stay4it.com/v1/public/core/?service=user.login";
        String content = "account=stay4it&password=123456";
        Request request = new Request(url, Request.RequestMethod.POST);
        request.setCallback(new JasonCallback<String>() {

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

    private void testHttpPostForDownload() {
        checkPermissions();
    }

    private void testHttpPostForDownloadInter() {
        String url = "http://api.stay4it.com/v1/public/core/?service=user.login";
        String content = "account=stay4it&password=123456";
        String path = Environment.getExternalStorageDirectory() + File.separator + "demo.txt";
        Request request = new Request(url, Request.RequestMethod.POST);
        request.setCallback(new FileCallback() {

            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "testHttpGet return:" + result);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        }.setCachePath(path));
        request.content = content;
        RequestTask task = new RequestTask(request);
        task.execute();
    }

    /**
     * @param
     * @return
     * Request read and write permissions
     */
    private void checkPermissions() {
        int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWritePermission != PackageManager.PERMISSION_GRANTED || hasReadPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSIONS);
            }
        } else {
            testHttpPostForDownloadInter();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
                boolean allPermissionGrant = true;
                for (int i = 0; i < permissions.length; i++) {
                    Log.d(TAG, "onRequestPermissionsResult: request permisiion = " + permissions[i]);
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        allPermissionGrant = false;
                    }

                }
                if (allPermissionGrant) {
                    // Permission Granted
                    // do something
                    testHttpPostForDownloadInter();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }
}
