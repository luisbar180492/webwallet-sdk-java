package com.minka;

import io.minka.api.handler.ApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.handler.TokenApi;
import io.minka.api.model.TokenResponse;

public class OauthClient {

    private static final String GRANT_TYPE = "client_credentials";

    private TokenApi api;

    private String clientId;
    private String secret;

    public OauthClient(String clientId, String secret, String oauthUrl, boolean debugging) {

        ApiClient apiClientToken = new ApiClient();
        apiClientToken.setDebugging(debugging);
        apiClientToken.setBasePath(oauthUrl);
        api = new TokenApi(apiClientToken);
        this.secret = secret;
        this.clientId = clientId;
    }

    public TokenResponse getToken() throws ApiException {
        return api.getToken(GRANT_TYPE, clientId, secret);
    }
}
