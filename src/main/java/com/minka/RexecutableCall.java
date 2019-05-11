package com.minka;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

public class RexecutableCall extends Call {

    private Call originalCall;
    private Request request;
    private RequestHolder requestHolder;

    protected RexecutableCall(OkHttpClient client, Request originalRequest) {
        super(client, originalRequest);
    }

    public RexecutableCall(OkHttpClient client, Call call, Request originalRequest, RequestHolder requestHolder){
        super(client, originalRequest);
        this.request = originalRequest;
        this.originalCall = call;
        this.requestHolder = requestHolder;
    }

    public Call getOriginalCall() {
        return originalCall;
    }

    public void setOriginalCall(Call originalCall) {
        this.originalCall = originalCall;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public RequestHolder getRequestHolder() {
        return requestHolder;
    }

    public void setRequestHolder(RequestHolder requestHolder) {
        this.requestHolder = requestHolder;
    }
}
