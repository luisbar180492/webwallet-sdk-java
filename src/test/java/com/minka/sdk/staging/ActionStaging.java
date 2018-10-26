package com.minka.sdk.staging;

import com.minka.ExceptionResponseTinApi;
import com.minka.sdk.ActionTesting;
import com.minka.sdk.TestingConstants;
import com.minka.utils.ActionType;
import com.minka.utils.AliasType;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.*;
import io.minka.api.model.PublicKeys;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Ignore;

public class ActionStaging {

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
    public void shouldCreateAction() throws io.minka.api.handler.ApiException {
        io.minka.api.model.CreateActionRequest req = new io.minka.api.model.CreateActionRequest();

        CreateActionRequestLabels labels = new CreateActionRequestLabels();
        req.setLabels(labels);
        req.setAmount("8");
        req.setSource("$ivanchonline");
        req.setSymbol("$tin");
        req.setTarget("$tin");
        System.out.println(req);

        io.minka.api.model.CreateActionResponse action = null;
        action = sdkApiClient.createAction(req);
        System.out.println(action.getActionId());
    }

    @Test
    public void getActionByActionId(){
        try {
            GetActionResponse actionResponse =
                    sdkApiClient.getAction(
                            "bd8518df-67c2-4598-b9fb-97b06c35c03e");

            System.out.println(actionResponse);
        } catch (ApiException e) {
            Logger.getLogger(ActionTesting.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Test
    public void shouldSignActionOnline(){
        String actionId = "bd8518df-67c2-4598-b9fb-97b06c35c03e";
        try {
            sdkApiClient.signAction(actionId, new ActionSignedLabels());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldGetPendingActions() throws ApiException {
        String handle = "$573207246903";
        List<GetActionResponse> genericResponse =
                sdkApiClient.getActionPendings(
                        handle, AliasType.TARGET, ActionType.SEND);

        System.out.println(genericResponse.size());
    }
}
