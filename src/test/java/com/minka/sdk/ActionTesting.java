package com.minka.sdk;


import com.minka.api.handler.ApiException;
import com.minka.api.model.CreateActionRequest;
import com.minka.api.model.CreateActionResponse;
import com.minka.api.model.GenericResponse;
import com.minka.wallet.primitives.utils.SdkApiClient;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ActionTesting {


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
    public void shouldCreateAction(){
        CreateActionRequest req = new CreateActionRequest();
        Map<String, Object> labels = new HashMap<>();

        req.setLabels(labels);
        req.setAmount("1");
        req.setSource("$tin");
        req.setSymbol("$tin");
        req.setTarget("$bh2nx6xx4");
        System.out.println(req);

        CreateActionResponse action = null;
        try {
            action = sdkApiClient.createAction(req);
        } catch (ApiException e) {
            System.out.println("e.getResponseBody()");
            System.out.println(e.getResponseBody());
//            e.printStackTrace();
        }
        System.out.println(action);
    }

    @Test
    public void shouldSignAction(){
        try {
            GenericResponse genericResponse = sdkApiClient.signAction("580ef951-f2f4-45ad-9646-bb471dceca8b");
            System.out.println(genericResponse);
        } catch (ApiException e) {
            e.printStackTrace();
        }

    }
}
