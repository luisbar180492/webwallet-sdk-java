package com.minka.sdk;


import com.minka.api.handler.ApiException;
import com.minka.api.model.SignerResponse;
import com.minka.wallet.primitives.utils.SdkApiClient;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SignerTesting {


    SdkApiClient sdkApiClient;

    @Before
    public void prepare(){

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN,
                TestingConstants.API_KEY);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

    }


    @Test
    public void createSignerForOnlineUse(){

        Map<String, Object> labels = new HashMap<>();
        labels.put("description", "limit");
        labels.put("bank_account_number", "BANK_LIMIT_ACCOUNT_NUMBER");
        try {
            SignerResponse signer = sdkApiClient.createSigner(labels);
            System.out.println(signer);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
        }
    }


}
