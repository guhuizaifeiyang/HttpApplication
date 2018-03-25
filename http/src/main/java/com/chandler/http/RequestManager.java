package com.chandler.http;

import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yaomenglin on 2018/3/25.
 */

public class RequestManager {
    private static RequestManager requestManager = new RequestManager();
    private final ExecutorService mRequestExecutor;
    private HashMap<String, ArrayList<Request>> mCacheRequestMap;

    public static RequestManager getInstance() {
        return requestManager;
    }

    public RequestManager() {
        mCacheRequestMap = new HashMap<String, ArrayList<Request>>();
        mRequestExecutor = Executors.newFixedThreadPool(5);
    }

    public void performRequest(Request request) {
        request.execute(mRequestExecutor);

        if (!mCacheRequestMap.containsKey(request.tag)){
            ArrayList<Request> requests = new ArrayList<Request>();
            mCacheRequestMap.put(request.tag, requests);
        }
        mCacheRequestMap.get(request.tag).add(request);

    }

    public void cancelRequest(String tag) {
        cancelRequest(tag, false);
    }
    public void cancelRequest(String tag, boolean force) {
        if (null == tag || "".equals(tag.trim())) {
            return;
        }

        if (mCacheRequestMap.containsKey(tag)){
            ArrayList<Request> requests = mCacheRequestMap.remove(tag);
            for (Request request : requests) {
                if (!request.isCancelled && tag.equals(request.tag)) {
                    request.cancel(force);
                }
            }
        }

    }

    public void cacelAll() {
        for (Map.Entry<String, ArrayList<Request>> entry : mCacheRequestMap.entrySet() ) {
            ArrayList<Request> requests = entry.getValue();
            for (Request request : requests) {
                request.cancel(true);
            }
        }
    }
}
