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

    }
    
//    @Test
//    public void shouldInitiateRequestTransfer(){
//        String handleTarget = "$3004431529";//to numero de telefono
//        String smsMessage = "ENVIO de";//solicit
//        String amount = "1";
//        String handleSourceAddress = "wQqRaa1fj8xtrMu68zo1vBE5SWaqpvZMda";
//        String action;
//        action = sdkApiClient.acceptTransferRequest(handleTarget, handleSourceAddress, amount, smsMessage);
//        System.out.println("ACTION ID " + action);
//        assertNotEquals(null, action);
//    }
    
    @Test
    public void shouldInitiateRequestTransfer(){
        String handleTarget = "$3004431529";//to numero de telefono
        String smsMessage = "ENVIO de";//solicit
        String amount = "1";
        String handleSourceAddress = "wQqRaa1fj8xtrMu68zo1vBE5SWaqpvZMda";
        String action;
        action = sdkApiClient.initiateTransferRequest(handleTarget, handleSourceAddress, amount, smsMessage);
        System.out.println("ACTION ID " + action);
        assertNotEquals(null, action);
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
}
