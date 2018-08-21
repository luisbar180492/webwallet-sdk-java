package com.minka.sdk;

import com.minka.api.handler.ApiException;
import com.minka.ExceptionResponseTinApi;
import com.minka.api.model.*;
import com.minka.utils.ActionType;
import com.minka.utils.AliasType;
import com.minka.wallet.primitives.utils.SdkApiClient;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertNotEquals;
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

        sdkApiClient.setBankLimitParams(TestingConstants.BANK_LIMIT_WALLET, TestingConstants.BANK_LIMIT_ADDRESS);
    }


    @Ignore
    @Test
    public void shouldConfirmRequestTransfer(){
        String handleTargetAddress = "wTEro49jTuEDmbsqKvehY2Khv533cnG6jA";
        String actionRequestId = "213e5f57-be2b-421e-ae45-ea908c8ef0bd";
        String action;
        action = sdkApiClient.confirmTransferRequest(handleTargetAddress, actionRequestId);
        System.out.println("ACTION ID " + action);
        assertNotEquals(null, action);
    }

    @Ignore
    @Test
    public void shouldAcceptRequestTransfer(){
        String handleTargetAddress = "wgqMLaKxbXy7STmLNwoUpjWEDzrdKJUtyk";
        String actionRequestId = "dff4423c-8c85-4902-a792-7c5168ba842d";
        String action;
        action = sdkApiClient.acceptTransferRequest(handleTargetAddress, actionRequestId);
        System.out.println("ACTION ID " + action);
        assertNotEquals(null, action);
    }

    @Ignore
    @Test
    public void shouldInitiateRequestTransfer(){
        String handleTarget = "$3104845181";//to numero de telefono
        String smsMessage = "solicitando 1 Tin";//solicit
        String amount = "1";
        String handleSourceAddress = "wgqMLaKxbXy7STmLNwoUpjWEDzrdKJUtyk";
        String action;
        action = sdkApiClient.createTransferRequest(handleTarget, handleSourceAddress, amount, smsMessage);
        System.out.println("ACTION ID " + action);
        assertNotEquals(null, action);
    }

    @Ignore
    @Test
    public void shouldInitiateNoTrustedTransfer(){
        String handleTarget = "$perro_mike";//to numero de telefono
        String smsMessage = "ENVIO de TINS";//solicit
        String amount = "1";
        String handleSourceAddress = "wgqMLaKxbXy7STmLNwoUpjWEDzrdKJUtyk";
        String action;
        action = sdkApiClient.createNoTrustedTransfer(handleTarget, handleSourceAddress, amount, smsMessage);
        System.out.println("ACTION ID " + action);
    }
    
    @Ignore
    @Test
    public void shouldConfirmTransfer(){
        String handleTargetAddress = "wgqMLaKxbXy7STmLNwoUpjWEDzrdKJUtyk";
        String actionRequestId = "49528514-48a4-4e8a-8111-92ea1e54abe4";
        String action;
        action = sdkApiClient.confirmTransfer(handleTargetAddress, actionRequestId);
        System.out.println("ACTION ID " + action);
        assertNotEquals(null, action);
    }
    
    @Ignore
    @Test
    public void shouldInitiateTransfer(){
        String handleTarget = "$3104845181";//to numero de telefono
        String smsMessage = "ENVIO de TINS prueba envío";//solicit
        String amount = "1";
        String handleSourceAddress = "wgqMLaKxbXy7STmLNwoUpjWEDzrdKJUtyk   ";
        String action;
        action = sdkApiClient.createTransfer(handleTarget, handleSourceAddress, amount, smsMessage);
        System.out.println("ACTION ID " + action);
        assertNotEquals(null, action);
    }
    

    @Test
    public void shouldRejectRequestTransfer(){
        String actionId = "60e34229-5fe4-46a5-9709-208e52bf9877";
        String address = "wd9jHDRK6AEmczb8n99QftrJTzDRMMitGq";

        try {
            sdkApiClient.rejectTransferRequest(address, actionId);
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }

    }
    @Ignore
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

    @Ignore
    @Test
    public void getActionByActionId(){
        try {
            GenericResponse actionResponse = sdkApiClient.getAction("e3979875-45ac-424e-bf23-3c50409437b2");
            System.out.println(actionResponse);
        } catch (ApiException ex) {
            Logger.getLogger(ActionTesting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Ignore
    @Test
    public void shouldSignAction(){
        try {
            GenericResponse genericResponse = sdkApiClient.signAction("580ef951-f2f4-45ad-9646-bb471dceca8b");
            System.out.println(genericResponse);
        } catch (ApiException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void shouldGetPendingActions(){
        String source = "$3104845181";
        PendingActionResponse genericResponse = sdkApiClient.getActionPendings(source, AliasType.TARGET, ActionType.REQUEST);
        System.out.println(genericResponse.size());

    }

    @Test
    public void shouldRejectTransferSend(){
        String addressForNotification= "wQqRaa1fj8xtrMu68zo1vBE5SWaqpvZMda";
        String sendActionId = "d6979617-28bb-40fa-87f3-0c9dfff0bbd5";
        try {
            sdkApiClient.rejectTransferSend(addressForNotification, sendActionId);
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }
    }
}
