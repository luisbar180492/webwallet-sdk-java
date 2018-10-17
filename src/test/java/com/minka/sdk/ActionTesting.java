package com.minka.sdk;

import com.minka.ExceptionResponseTinApi;
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

public class ActionTesting {


    SdkApiClient sdkApiClient;

    @Before
    public void prepare(){

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN,
                TestingConstants.API_KEY);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        if (TestingConstants.proxy){
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST,
                    TestingConstants.PROXY_PORT);
        }
    }

    @Test
    public void shouldCreateAction() throws io.minka.api.handler.ApiException {
        io.minka.api.model.CreateActionRequest req = new io.minka.api.model.CreateActionRequest();

        CreateActionRequestLabels labels = new CreateActionRequestLabels();
        req.setLabels(labels);
        req.setAmount("1");
        req.setSource("$offline");
        //req.setSource("$tin");
        req.setSymbol("$tin");
        req.setTarget("$ivanchonline");
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
        String actionId = "512884a6-5f94-4678-a1c3-226621275dce";
        try {
            ActionSignedLabels signedLabels = new ActionSignedLabels();
            ActionSigned actionSigned = sdkApiClient.signAction(actionId, signedLabels);
            System.out.println(actionSigned);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldSignActionOffline(){
        String actionId = "a3d0d6d2-6600-414f-b192-9b3fa3a3afe4";

        try {
            OfflineSigningKeys keys = new OfflineSigningKeys();
            List<PublicKeys> keeper = new ArrayList<>();
            PublicKeys theKeys = new PublicKeys();
            theKeys.setPublic("0407605f6a4a7b16784418c428ffcbc010ac703e0a92b104f7ed7eaa82ae64648b245ef22efa9919bf3f628d785552cb65faecd7332a383d0063506dc34ca79634");
            theKeys.setScheme("ed25519");
            theKeys.setSecret("0b311a83c863f8dd28f0b35be84b7bc3affc4b95e30bdfcc59ec7f23c389ba7c");
            keeper.add(theKeys);
            keys.setKeeper(keeper);
            ActionSigned actionSigned = sdkApiClient.signActionOffline(actionId, keys);

            System.out.println( actionSigned );

        } catch (ApiException e) {
            e.printStackTrace();
            System.out.println(e.getResponseBody());
            System.out.println(e.getCode());

        }

    }
    @Test
    public void shouldGetPendingActions(){
        String handle = "$573207246903";
        List<GetActionResponse> genericResponse =
                sdkApiClient.getActionPendings(
                handle, AliasType.TARGET, ActionType.SEND);

        System.out.println(genericResponse.size());
    }
}
