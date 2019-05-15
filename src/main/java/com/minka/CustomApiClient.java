package com.minka;

import com.squareup.okhttp.*;
import io.minka.api.handler.*;
import io.minka.api.handler.auth.ApiKeyAuth;
import io.minka.api.handler.auth.OAuth;
import io.minka.api.model.TokenResponse;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class CustomApiClient extends ApiClient {

    private String oauthUrl;
    private String clientId;
    private String secret;

    @Override
    public String buildUrl(String path, List<Pair> queryParams, List<Pair> collectionQueryParams) {

        if (queryParams.size() ==1 && queryParams.get(0).getName().equals("customQuery"))
        {
            return super.getBasePath() + path + queryParams.get(0).getValue();
        } else if (path.contains("/custom/action/")){
            return super.buildUrl(StringUtils.remove(path,"/custom"), queryParams, collectionQueryParams);
        }else if (path.contains("/custom/signer")){
            return super.buildUrl(StringUtils.remove(path,"/custom"), queryParams, collectionQueryParams);
        } else {
            return super.buildUrl(path, queryParams, collectionQueryParams);
        }
    }

    @Override
    public Call buildCall(String path, String method, List<Pair> queryParams, List<Pair> collectionQueryParams, Object body, Map<String, String> headerParams, Map<String, Object> formParams, String[] authNames, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Request request = buildRequest(path, method, queryParams, collectionQueryParams, body, headerParams, formParams, authNames, progressRequestListener);

        Call call = this.getHttpClient().newCall(request);
        RequestHolder requestHolder = new RequestHolder(path, method, queryParams, collectionQueryParams, body, headerParams, formParams, authNames, progressRequestListener);
        return new RexecutableCall(this.getHttpClient(), call, request, requestHolder);
    }

    private void updateHeaders() throws ApiException {
//        super.setDebugging(true);
        OAuth oauth2 = (OAuth) super.getAuthentication("oAuth2ClientCredentials");
        if (oauth2.getAccessToken() == null){
            TokenResponse token = fetchToken();
            this.setAccessToken(token.getAccessToken());
        } else {
            // no need
        }
        ApiKeyAuth xNonce = (ApiKeyAuth) this.getAuthentication("XNonce");
        xNonce.setApiKey(generateUUID());
    }

    public String generateUUID() {
        return String.valueOf((new Date()).getTime()) + "-" +
                UUID.randomUUID().toString();
    }

    @Override
    public <T> ApiResponse<T> execute(Call call, Type returnType) throws ApiException {
        T data;
        updateHeaders();
        try {
            Response response = call.execute();
            if (isTheTokenInvalid(response.code())){
                TokenResponse token = fetchToken();
                this.setAccessToken(token.getAccessToken());

                Request request = updateTokenAuthenticationInRequest((RexecutableCall) call, token);
                Response resp = this.getHttpClient().newCall(request).execute();
                data = handleResponse(resp, returnType);
            } else {
                data = handleResponse(response, returnType);
            }
            return new ApiResponse<T>(response.code(), response.headers().toMultimap(), data);
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    private Request updateTokenAuthenticationInRequest(RexecutableCall call, TokenResponse token) throws ApiException {
        RexecutableCall callAgain = call;
        RequestHolder req= callAgain.getRequestHolder();
        Map<String, String> headerParamsWithToken = req.getHeaderParams();
        headerParamsWithToken.put("Authorization", "Bearer " + token.getAccessToken());

        return buildRequest(req.getPath(), req.getMethod(), req.getQueryParams(), req.getCollectionQueryParams(),
                req.getBody(), headerParamsWithToken , req.getFormParams(), req.getAuthNames(), req.getProgressRequestListener());
    }

    private TokenResponse fetchToken() throws ApiException {
        OauthClient oauthClient = new OauthClient(clientId, secret, oauthUrl);
        return oauthClient.getToken();
    }

    private boolean isTheTokenInvalid(int code) {
        return code == 403;
    }

    public void setOauthUrl(String oauthUrl) {
        this.oauthUrl = oauthUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}