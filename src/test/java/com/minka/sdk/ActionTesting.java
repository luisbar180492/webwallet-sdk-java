package com.minka.sdk;

import com.minka.utils.ActionType;
import com.minka.utils.AliasType;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.*;
import io.minka.api.model.PublicKeys;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActionTesting {


    SdkApiClient sdkApiClient;

    @Before
    public void prepare(){

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_TESTING,
                TestingConstants.API_KEY, TestingConstants.TESTING_BASE);


        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        if (TestingConstants.proxy){
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST,
                    TestingConstants.PROXY_PORT);
        }
    }

    @Ignore
    @Test
    public void shouldCreateAction() throws io.minka.api.handler.ApiException {
        io.minka.api.model.CreateActionRequest req = new io.minka.api.model.CreateActionRequest();

        CreateActionRequestLabels labels = new CreateActionRequestLabels();
        req.setLabels(labels);
        req.setAmount("100");
        //req.setSource("$gzlboubhabkmslivdxt");
        req.setSource("$tin");
        req.setSymbol("$tin");
        req.setTarget("$gzlboubhabkmslivdxt");
        System.out.println(req);

        io.minka.api.model.CreateActionResponse action = null;
        action = sdkApiClient.createAction(req);
        System.out.println(action.getActionId());
    }

    @Test
    public void getActionByActionsNoFilters() {
        try {
            GetTransfersResponse actions = sdkApiClient.getActions();
            System.out.println(actions.getEntities().size());
        } catch (ApiException e) {
            e.printStackTrace();
            System.out.println(e.getResponseBody());
        }
    }

    @Test
    public void getTransfersWithCustomQuery() {
        try {
            String query = "?";
            Transfers actions = sdkApiClient.getTransfersWithCustomQuery(query);
            System.out.println(actions.getEntities().size());
        } catch (ApiException e) {
            e.printStackTrace();
            System.out.println(e.getResponseBody());
        }
    }

    @Test
    public void getActionsWithCustomQuery() {
        try {
            String query = "?labels.type=SEND&target=$573008507524";
            GetTransfersResponse actions = sdkApiClient.getActionsWithCustomQuery(query);
            System.out.println(actions.getEntities().size());
        } catch (ApiException e) {
            e.printStackTrace();
            System.out.println(e.getResponseBody());
        }
    }

    @Test
    public void getActionByActionId(){
        try {
            GetActionResponse actionResponse =
                    sdkApiClient.getAction(
                            "3eafded6-2645-4ce2-836c-c79928d8df77");

            System.out.println(actionResponse);
        } catch (ApiException e) {
            Logger.getLogger(ActionTesting.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Test
    public void shouldGenerateUUID(){
        String nonceGenerated = sdkApiClient.generateUUID();
        System.out.println(nonceGenerated);
        System.out.println(nonceGenerated.length());

    }


    @Test
    public void shouldSignActionOnline(){
        String actionId = "b32debab-9df0-49db-814f-92a06dbedc15";
        try {
            ActionSignedLabels signedLabels = new ActionSignedLabels();
            ActionSigned actionSigned = sdkApiClient.signAction(actionId, signedLabels);
            System.out.println(actionSigned);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
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
    public void shouldGetPendingActions() {
        String handle = "$573008507524";

        try {
            List<GetActionResponse> genericResponse =
                    sdkApiClient.getActionPendings(handle, AliasType.TARGET, ActionType.SEND);
            System.out.println(genericResponse.size());
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
        }

    }
}
