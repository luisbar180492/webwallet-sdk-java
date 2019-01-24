package com.minka;

import com.minka.utils.*;
import com.minka.utils.Constants;
import com.squareup.okhttp.Call;
import io.minka.api.handler.*;
import junit.framework.Test;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class CustomApiClient extends ApiClient {

    @Override
    public String buildUrl(String path, List<Pair> queryParams, List<Pair> collectionQueryParams) {

        if (queryParams.size() ==1 && queryParams.get(0).getName().equals("customQuery"))
        {
            return super.getBasePath() + path + queryParams.get(0).getValue();
        } else if (path.contains("/custom/signer")){

            return super.buildUrl(StringUtils.remove(path,"/custom"), queryParams, collectionQueryParams);
        } else {
            return super.buildUrl(path, queryParams, collectionQueryParams);
        }
    }

    /*
    @Override
    public Call buildCall(String path, String method, List<Pair> queryParams, List<Pair> collectionQueryParams, Object body, Map<String, String> headerParams, Map<String, Object> formParams, String[] authNames, ProgressRequestBody.ProgressRequestListener progressRequestListener)  {

        try {
            return super.buildCall(path, method, queryParams, collectionQueryParams, body, headerParams, formParams, authNames, progressRequestListener);
        } catch (ApiException e) {
            if (e.getCode() == Constants.BAD_REQUEST && e.getResponseBody().contains(Constants.MESSAGE_OAUTH)){
                //fetchOauthToken();
            };
        }
    }

    */
/*
    private void fetchOauthToken() {
        ApiClient apiClientToken = new ApiClient();

        apiClientToken.setUsername(clientId);
        apiClientToken.setPassword(secret);

        apiClientToken.setBasePath(this.url.substring(0, this.url.length() - 3));
        TokenApi api = new TokenApi(apiClientToken);
        return api.getToken("client_credentials", clientId, secret);
    }
*/
}
