package com.chandler.httpapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chandler.http.AppException;
import com.chandler.http.FileCallback;
import com.chandler.http.JasonCallback;
import com.chandler.http.JsonArrayReaderCallback;
import com.chandler.http.Request;
import com.chandler.http.RequestManager;
import com.chandler.http.RequestTask;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_PERMISSIONS = 123;
    private static final String TAG = "chandler";
    private static boolean mPermissionGranted = false;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.btn);
        mButton.setOnClickListener(this);

        checkPermissions();

    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: permission = " + mPermissionGranted);
        if (!mPermissionGranted) {
            return;
        }
        switch (view.getId()) {
            case R.id.btn:
//                testHttpPostOnSubThread();
//                testHttpPostOnSubThreadForGeneric();
//                testHttpPostForDownloadProgress();
//                testHttpPostForDownloadProgressCancelTest();
//                testHttpPostOnSubThreadForGenericLoadMore();
                testHttpForJsonReaderArray();
        }

    }

    public void testHttpForJsonReaderArray() {
        String url = "http://api.stay4it.com/v1/public/core/?service=user.getAll";
        url += "&timestamp=" + System.currentTimeMillis() + "&count=30";
        Request request = new Request(url, Request.RequestMethod.GET);
        String path = Environment.getExternalStorageDirectory() + File.separator + "jsonarray.tmp";
        request.setCallback(new JsonArrayReaderCallback<Module>() {

            @Override
            public ArrayList<Module> preRequest() {
//                TODO fetch data
                return null;
            }

            @Override
            public ArrayList<Module> postRequest(ArrayList<Module> modules) {
//                TODO insert into db
                return modules;
            }

            @Override
            public void onSuccess(ArrayList<Module> result) {
                Log.d("chandler", "result " + result.size());


            }

            @Override
            public void onFailure(AppException e) {
                e.printStackTrace();
            }
        }.setCachePath(path));
        RequestManager.getInstance().performRequest(request);

    }

    private void testHttpPostOnSubThreadForGenericLoadMore() {
        String url = "http://api.stay4it.com/v1/public/core/?service=user.getAll";
        url += "&timestamp=" + System.currentTimeMillis() + "&count=30";
        Request request = new Request(url, Request.RequestMethod.GET);
        request.setCallback(new JasonCallback<ArrayList<Module>>() {
            @Override
            public ArrayList<Module> preRequest() {
                return null;
            }

            @Override
            public ArrayList<Module> postRequest(ArrayList<Module> modules) {
                return modules;
            }

            @Override
            public void onSuccess(ArrayList<Module> result) {
                Log.d("chandler", "testHttpGet return:" + result);
            }

            @Override
            public void onFailure(AppException e) {
                e.printStackTrace();
            }
        });
        RequestTask task = new RequestTask(request);
        task.execute();
    }

    private void testHttpPostOnSubThreadForGeneric() {
        String url = "http://api.stay4it.com/v1/public/core/?service=user.login";
        String content = "account=stay4it&password=123456";
        Request request = new Request(url, Request.RequestMethod.POST);
        request.setCallback(new JasonCallback<User>() {

            @Override
            public void onSuccess(User result) {
                Log.d("chandler", "testHttpGet return:" + result);
            }

            @Override
            public void onFailure(AppException e) {
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
            public void onFailure(AppException e) {
                e.printStackTrace();
            }
        });
        request.content = content;
        RequestTask task = new RequestTask(request);
        task.execute();
    }


    private void testHttpPostForDownloadProgress() {
        Log.d(TAG, "testHttpPostForDownloadProgress: ");
        String url = "http://api.stay4it.com/uploads/test.jpg";
        String path = Environment.getExternalStorageDirectory() + File.separator + "demo.txt";
        Request request = new Request(url, Request.RequestMethod.GET);
        request.setEnableProgress(true);
        request.setCallback(new FileCallback() {

            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "testHttpGet return:" + result);
            }

            @Override
            public void onFailure(AppException e) {
                Log.d(TAG, "onFailure: status code = " + e.statusCode + ", message = " + e.responseMessage);
                e.printStackTrace();
            }

            @Override
            public void onProgressUpdated(int curLen, int totalLen) {
                Log.d(TAG, "onProgressUpdated: curLen = " + curLen + ", totalLen = " + totalLen);
            }
        }.setCachePath(path));
        request.setOnGlobalExceptionListener(this);
        RequestTask task = new RequestTask(request);
        task.execute();
    }

    private void testHttpPostForDownloadProgressCancelTest() {
        Log.d(TAG, "testHttpPostForDownloadProgressCancelTest: ");
        String url = "http://a.hiphotos.baidu.com/image/h%3D300/sign=a18b980dbd3533faeab6952e98d3fdca/9f510fb30f2442a76160eca6dd43ad4bd1130242.jpg";
        String path = Environment.getExternalStorageDirectory() + File.separator + "demo.txt";

        final Request request = new Request(url, Request.RequestMethod.GET);
        request.setOnGlobalExceptionListener(this);
        request.setEnableProgress(true);
        request.setCallback(new FileCallback() {

            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "testHttpGet return:" + result);
            }

            @Override
            public void onFailure(AppException e) {
                Log.d(TAG, "onFailure: status code = " + e.statusCode + ", message = " + e.responseMessage);
                e.printStackTrace();
            }

            @Override
            public void onProgressUpdated(int curLen, int totalLen) {
                Log.d(TAG, "onProgressUpdated: curLen = " + curLen + ", totalLen = " + totalLen);
                if (curLen * 100L / totalLen > 50) {
//                    request.cancel();
                }
            }
        }.setCachePath(path));
        request.setTag(toString());
        RequestManager.getInstance().performRequest(request);
    }

    private void testHttpPostForDownload() {
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
            public void onFailure(AppException e) {
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
        Log.d(TAG, "checkPermissions: ");
        int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWritePermission != PackageManager.PERMISSION_GRANTED || hasReadPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSIONS);
            }
        } else {
            mPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: request code = " + requestCode);
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
                    mPermissionGranted = true;
                } else {
                    // Permission Denied
                    mPermissionGranted = false;
                    Toast.makeText(MainActivity.this, "PERMISSIONS Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }
}
