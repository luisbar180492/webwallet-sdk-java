package com.minka;

import io.minka.api.handler.Pair;
import io.minka.api.handler.ProgressRequestBody;

import java.util.List;
import java.util.Map;

public class RequestHolder {

    private final String path;
    private final String method;
    private final List<Pair> queryParams;
    private final List<Pair> collectionQueryParams;
    private final Object body;
    private final Map<String, String> headerParams;
    private final String[] authNames;
    private final Map<String, Object> formParams;
    private final ProgressRequestBody.ProgressRequestListener progressRequestListener;

    public RequestHolder(String path, String method, List<Pair> queryParams, List<Pair> collectionQueryParams,
                         Object body, Map<String, String> headerParams, Map<String, Object> formParams, String[] authNames,
                         ProgressRequestBody.ProgressRequestListener progressRequestListener) {

        this.path = path;
        this.method = method;
        this.queryParams = queryParams;
        this.collectionQueryParams = collectionQueryParams;
        this.body = body;
        this.headerParams = headerParams;
        this.formParams = formParams;
        this.authNames = authNames;
        this.progressRequestListener = progressRequestListener;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public List<Pair> getQueryParams() {
        return queryParams;
    }

    public List<Pair> getCollectionQueryParams() {
        return collectionQueryParams;
    }

    public Object getBody() {
        return body;
    }

    public Map<String, String> getHeaderParams() {
        return headerParams;
    }

    public String[] getAuthNames() {
        return authNames;
    }

    public Map<String, Object> getFormParams() {
        return formParams;
    }

    public ProgressRequestBody.ProgressRequestListener getProgressRequestListener() {
        return progressRequestListener;
    }
}
