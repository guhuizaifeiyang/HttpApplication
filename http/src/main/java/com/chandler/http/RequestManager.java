package com.chandler.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaomenglin on 2018/3/25.
 */

public class RequestManager {
    private static RequestManager requestManager = new RequestManager();
    private HashMap<String, ArrayList<Request>> mCacheRequestMap;

    public static RequestManager getInstance() {
        return requestManager;
    }

    public RequestManager() {
        mCacheRequestMap = new HashMap<String, ArrayList<Request>>();
    }

    public void performRequest(Request request) {
        RequestTask task = new RequestTask(request);
        task.execute();
        if (!mCacheRequestMap.containsKey(request.tag)){
            ArrayList<Request> requests = new ArrayList<Request>();
            mCacheRequestMap.put(request.tag, requests);
        }
        mCacheRequestMap.get(request.tag).add(request);

    }

    public void cancelRequest(String tag) {
        if (null == tag || "".equals(tag.trim())) {
            return;
        }

        if (mCacheRequestMap.containsKey(tag)){
            ArrayList<Request> requests = mCacheRequestMap.remove(tag);
            for (Request request : requests) {
                if (!request.isCancelled && tag.equals(request.tag)) {
                    request.cancel();
                }
            }
        }

    }

    public void cacelAll() {
        for (Map.Entry<String, ArrayList<Request>> entry : mCacheRequestMap.entrySet() ) {
            ArrayList<Request> requests = entry.getValue();
            for (Request request : requests) {
                request.cancel();
            }
        }
    }
}
