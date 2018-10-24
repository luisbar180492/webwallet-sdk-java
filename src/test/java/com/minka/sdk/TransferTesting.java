package com.minka.sdk;

import com.minka.ExceptionResponseTinApi;
import com.minka.utils.ActionType;
import com.minka.utils.AliasType;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransferTesting {

    SdkApiClient sdkApiClient;

    @Before
    public void prepare(){

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN,
                TestingConstants.API_KEY);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);
        if (TestingConstants.proxy){
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST, TestingConstants.PROXY_PORT);
        }


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

        //walletObject.setLabels(labels);
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

    @Ignore
    @Test
    public void continueP2Ptransfer(){
        ActionSigned actionSigned = new ActionSigned();
        actionSigned.setActionId("83161a46-b797-44bb-b006-c4bd984fc9f9");
        actionSigned.setAmount("1");
        actionSigned.setSource("wM9kx8Bzsp51TRefSccAhH57KU4a2rfS5y");
        actionSigned.setSymbol("$tin");
        actionSigned.setTarget("wi5pLSsmkEwP4KHtfHnudLKDWBf59LViuZ");
        Snapshot snapshot = new Snapshot();

        actionSigned.setSnapshot(snapshot);
        ActionSignedLabels labels = new ActionSignedLabels();
        labels.setTxRef("15403041367819472");
        labels.setType("DOWNLOAD");
        labels.setHash("5eb70f98bb90459c267ad0ac2d7610b78d27b9f7b1001bf2032a9dac8025f688");

        actionSigned.setLabels(labels);

        String actionId = "ea80e7a9-87e2-4063-8d37-6348658fd3fe";

        try {
            GetActionResponse getActionResponse = sdkApiClient.continueTransaction(actionId, actionSigned);
            System.out.println(getActionResponse);
        } catch (ApiException e) {
            e.printStackTrace();
        }

    }

    @Ignore
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