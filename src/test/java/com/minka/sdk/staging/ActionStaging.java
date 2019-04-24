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
    public void prepare(){

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_STAGING,
                TestingConstants.API_KEY, TestingConstants.STAGING_BASE);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

    }

    @Test
    public void shouldCreateAction() {
        SdkApiClient sdkApiClient;
        sdkApiClient = new SdkApiClient("achtin.minka.io",
                "a34Bd8ADBc9bd6aDaDB8CC49Bbd9f88EA3CEe3AaeC93AcDA516504Ce", TestingConstants.DEV_BASE);

        sdkApiClient
                .setSecret("DecE44EbaeEcAE402DD07e3BE3Ad48DD")
                .setClientId("91265AF5907c4cFdB7D8Ba7A7EEe209851beBFaCCA3D434F");


        String actionId = "e8c1267c-4164-4494-b8eb-077b20ebd0b8";

        OfflineSigningKeys keys = new OfflineSigningKeys();

        PublicKeys key = new PublicKeys();
        key.setPublic("49a1d0752e9a11a4e013dd1c9064e526f6cd457050b71b043eb96e21f1f88400");
        key.setSecret("23e4352c1cd05122923656de085e85ea70c38ecb613d29c1a705c20322697a42");
        key.setScheme("eddsa-ed25519");
        keys.addKeeperItem(key);

        CreateActionRequest req = new CreateActionRequest();
        CreateActionRequestLabels labels = new CreateActionRequestLabels();
        labels.setTxRef("sdfa");
        labels.setType("SEND");
        DeviceFingerPrint device = new DeviceFingerPrint();
        labels.setDeviceFingerPrint(device);
        labels.setNumberOftransactions("1");

        req.setLabels(labels);

//        source: wdG1Ntyg3hehVnibsgkCVv772ccwyj7rCo
//        symbol:
//        target: wW8yxrKWppeQjhDEpS7TJTR8XmCufzZb8H
        req.setSource("wdG1Ntyg3hehVnibsgkCVv772ccwyj7rCo");
        req.setSymbol("$tin");
        req.setAmount("12");
        req.setTarget("wW8yxrKWppeQjhDEpS7TJTR8XmCufzZb8H");
        try {
            CreateActionResponse action = sdkApiClient.createAction(req);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
        }

        /*
        try {
            //GetActionResponse action = sdkApiClient.getAction(actionId);
            ActionSigned actionSigned = sdkApiClient.signActionOffline(actionId, keys);
            System.out.println(actionSigned);
        } catch (ApiException e) {

            System.out.println(e.getResponseBody());
        }
       */
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
