package com.minka.sdk.staging;

import com.minka.sdk.ActionTesting;
import com.minka.sdk.TestingConstants;
import com.minka.utils.ActionType;
import com.minka.utils.AliasType;
import com.minka.wallet.MissingRequiredParameterIOUCreation;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActionStaging {

    SdkApiClient sdkApiClient;

    @Before
    public void prepare() throws ApiException {

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_STAGING,
                TestingConstants.API_KEY, TestingConstants.STAGING_BASE);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

    }

    @Test
    public void shouldCreateAction() throws io.minka.api.handler.ApiException {
        io.minka.api.model.CreateActionRequest req = new io.minka.api.model.CreateActionRequest();

        CreateActionRequestLabels labels = new CreateActionRequestLabels();
        req.setLabels(labels);
        req.setAmount("1");
        req.setSource("$bancoheroku");
        req.setSymbol("$tin");
        req.setTarget("$573104845181");
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
                            "6e0e2829-a9d5-4977-a0ff-4afc5f07ac43");

            System.out.println(actionResponse);
        } catch (ApiException e) {
            Logger.getLogger(ActionTesting.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Test
    public void shouldSignActionOffline() throws MissingRequiredParameterIOUCreation {
        String actionId = "5528cd6d-9eeb-40f3-921a-9483cc34628e";

        try {
            OfflineSigningKeys keys = new OfflineSigningKeys();
            List<PublicKeys> keeper = new ArrayList<>();
            PublicKeys theKeys = new PublicKeys();
            theKeys.setPublic("73962f7467934bcd42d9c9e4a65efa5002c20b390d90f474f7c7e74d9c439cc3");
            theKeys.setScheme("eddsa-ed25519");
            theKeys.setSecret("0bf2ee2e4847991be298b0b28f6bf254de5e8479af16f7f14ac2c816adb68ff6");
            keeper.add(theKeys);
            keys.setKeeper(keeper);
            ActionSigned actionSigned = sdkApiClient.signActionOffline(actionId, keys);

            System.out.println( actionSigned );

        } catch (ApiException e) {
            System.out.println("e.getResponseBody()");

            e.printStackTrace();
            System.out.println(e.getResponseBody());
            System.out.println(e.getCode());

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
