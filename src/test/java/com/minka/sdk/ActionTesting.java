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

    }


    @Ignore
    @Test
    public void shouldConfirmRequestTransfer(){
        String handleTargetAddress = "wTEro49jTuEDmbsqKvehY2Khv533cnG6jA";
        String actionRequestId = "213e5f57-be2b-421e-ae45-ea908c8ef0bd";
        String action;
//        action = sdkApiClient.confirmTransferRequest(handleTargetAddress, actionRequestId);
//        System.out.println("ACTION ID " + action);
//        assertNotEquals(null, action);
    }

    @Ignore
    @Test
    public void shouldAcceptRequestTransfer() throws io.minka.api.handler.ApiException {

        String actionRequestId = "dff4423c-8c85-4902-a792-7c5168ba842d";

        AcceptTransferRequest req = new AcceptTransferRequest();
        SignerObject signer = new SignerObject();
        signer.setHandle("wic6kF3M2Zvy986mByV76YKXDFNY1spa6f");
        req.setSigner(signer);
        WalletObject walletObject = new WalletObject();
        Map<String, Object> labels = new HashMap<>();
        labels.put("created", "2018-08-29T06:09:35.408Z");
        labels.put("channelSms", "573185951061");

        walletObject.setLabels(labels);
        walletObject.setHandle("$573185951061");
        List<String> signerss = new ArrayList<>();
        signerss.add("wic6kF3M2Zvy986mByV76YKXDFNY1spa6f");
        walletObject.setSigner(signerss);
        req.setWallet(walletObject);

        CreateTransferResponse response;
        response = sdkApiClient.acceptTransfer(req, actionRequestId);
        System.out.println(response);

//        UpdateActionRequest req = new UpdateActionRequest();
//        Map<String, Object> labels = new HashMap<>();
//        labels.put("key", "value");
//        req.setLabels(labels);
//        Snapshot snapshot = new Snapshot();
//        req.setSnapshot(snapshot);
//        sdkApiClient.updateAction(actionRequestId,req);
    }

    @Ignore
    @Test
    public void shouldInitiateRequestTransfer(){
        String handleTarget = "$3104845181";//to numero de telefono
        String smsMessage = "solicitando 1 Tin";//solicit
        String amount = "1";
        String handleSourceAddress = "wgqMLaKxbXy7STmLNwoUpjWEDzrdKJUtyk";
        String action;
//        action = sdkApiClient.createTransferRequest(handleTarget, handleSourceAddress, amount, smsMessage);
//        System.out.println("ACTION ID " + action);
//        assertNotEquals(null, action);
    }

    @Ignore
    @Test
    public void shouldInitiateNoTrustedTransfer(){
        String handleTarget = "$perro_mike";//to numero de telefono
        String smsMessage = "ENVIO de TINS";//solicit
        String amount = "1";
        String handleSourceAddress = "wgqMLaKxbXy7STmLNwoUpjWEDzrdKJUtyk";
        String action;
//        action = sdkApiClient.createNoTrustedTransfer(handleTarget, handleSourceAddress, amount, smsMessage);
//        System.out.println("ACTION ID " + action);
    }
    
    @Ignore
    @Test
    public void shouldConfirmTransfer(){
        String handleTargetAddress = "wgqMLaKxbXy7STmLNwoUpjWEDzrdKJUtyk";
        String actionRequestId = "49528514-48a4-4e8a-8111-92ea1e54abe4";
        String action;
//        action = sdkApiClient.confirmTransfer(handleTargetAddress, actionRequestId);
//        System.out.println("ACTION ID " + action);
//        assertNotEquals(null, action);
    }
    
    @Ignore
    @Test
    public void shouldInitiateTransfer(){
        String handleTarget = "$3104845181";//to numero de telefono
        String smsMessage = "ENVIO de TINS prueba envío";//solicit
        String amount = "1";
        String handleSourceAddress = "wgqMLaKxbXy7STmLNwoUpjWEDzrdKJUtyk   ";
        String action;
//        action = sdkApiClient.createTransfer(handleTarget, handleSourceAddress, amount, smsMessage);
//        System.out.println("ACTION ID " + action);
//        assertNotEquals(null, action);
    }
    

    @Ignore
    @Test
    public void shouldRejectRequestTransfer(){
        String actionId = "60e34229-5fe4-46a5-9709-208e52bf9877";

        /*
        try {
            sdkApiClient.rejectTransferRequest(actionId);
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }
        */
    }

    @Test
    public void shouldCreateAction() throws  io.minka.api.handler.ApiException {
        io.minka.api.model.CreateActionRequest req = new io.minka.api.model.CreateActionRequest();

        CreateActionRequestLabels labels = new CreateActionRequestLabels();
        req.setLabels(labels);
        req.setAmount("8");
        req.setSource("$ivanchonline");
        req.setSymbol("$tin");
        req.setTarget("$tin");
        System.out.println(req);

        io.minka.api.model.CreateActionResponse action = null;
//        try {
        action = sdkApiClient.createAction(req);
//        } catch (ApiException e) {
//            System.out.println("e.getResponseBody()");
//            System.out.println(e.getResponseBody());
////            e.printStackTrace();
//        }
        System.out.println(action);
    }

    @Ignore
    @Test
    public void getActionByActionId(){
        try {
            GetActionResponse actionResponse = sdkApiClient.getAction("e3979875-45ac-424e-bf23-3c50409437b2");
            System.out.println(actionResponse);
        } catch (ApiException e) {
            Logger.getLogger(ActionTesting.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    @Ignore
    @Test
    public void shouldSignAction(){

        try {
            String actionId = "7abe3ffb-a7ca-43b4-b994-fefeb35c352d";
            OfflineSigningKeys keys = new OfflineSigningKeys();
            List<PublicKeys> keepers = new ArrayList<>();
            io.minka.api.model.PublicKeys currKeeper = new PublicKeys();
            currKeeper.setScheme("ed25519");
            currKeeper.setSecret("069976466bbae2180db0d61c3bbbbbbe54f61385c364e24de4bd0ce607d94d9a");
            currKeeper.setPublic("0445d1e984ba787e960f28e70fcfabd61cc7dda45d9ae94de0cec7daa0e91da7be161d39451024c2a4f1998877819038f607d9c2016250b58e6b463344e051bb01");
            keepers.add(currKeeper);
            keys.setKeeper(keepers);
            CreateTransferResponse genericResponse = sdkApiClient.signActionOffline(actionId, keys);

            System.out.println(genericResponse);
        } catch (io.minka.api.handler.ApiException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void shouldGetActionWithFilters() {
        Map<String, Object> filters;
        filters = new HashMap<>();

//        sdkApiClient.getActionsFiltered(filters);

        GetTransfersResponse actionsFiltered = null;
//        try {
//            actionsFiltered = sdkApiClient.getActionsFiltered();
//            System.out.println(actionsFiltered);

//            int size = actionsFiltered.getEntities().size();
//            if(actionsFiltered.getEntities() != null){
//                System.out.println(actionsFiltered.getEntities().size());
//            }
//
//            System.out.println("Size: " + size);

//        } catch (io.minka.api.handler.ApiException e) {
//            System.out.println(e.getResponseBody());
//            e.printStackTrace();
//        }
    }

    @Test
    public void shouldGetPendingActions(){
        String handle = "$573207246903";
        List<GetActionResponse> genericResponse = sdkApiClient.getActionPendings(
                handle, AliasType.TARGET, ActionType.SEND);

        System.out.println(genericResponse);


    }

    @Test
    public void shouldRejectTransferSend(){
        String addressForNotification= "wQqRaa1fj8xtrMu68zo1vBE5SWaqpvZMda";
        String sendActionId = "d6979617-28bb-40fa-87f3-0c9dfff0bbd5";
        try {
            RejectTransferRequest req = new RejectTransferRequest();
            sdkApiClient.rejectTransfer(req, sendActionId);
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        } catch (io.minka.api.handler.ApiException e) {
            e.printStackTrace();
        }
    }
}
