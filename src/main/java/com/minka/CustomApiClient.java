package com.minka;

import io.minka.api.handler.*;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class CustomApiClient extends ApiClient {

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

}
