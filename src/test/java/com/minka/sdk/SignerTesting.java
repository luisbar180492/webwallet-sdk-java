package com.minka.sdk;


import com.minka.ExceptionResponseTinApi;
import com.minka.api.handler.ApiException;
import com.minka.api.model.SignerResponse;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.model.SignerListResponse;
import io.minka.api.model.SignerRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SignerTesting {


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
    public void shouldNotifyBank(){
        String actionId = "ab8d135f-736a-4a0f-bea3-ad38c3f75267";
        String solicitanteAddress = "wd9jHDRK6AEmczb8n99QftrJTzDRMMitGq";
        try {
            sdkApiClient.notifyBankToDownload(solicitanteAddress, actionId);
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }
    }

    @Test
    public void shouldNotifyStatusToBank() throws io.minka.api.handler.ApiException {
        String actionId = "ab8d135f-736a-4a0f-bea3-ad38c3f75267";
        String solicitanteAddress = "wd9jHDRK6AEmczb8n99QftrJTzDRMMitGq";
        try {
            sdkApiClient.notifyStatusToBank(solicitanteAddress, actionId);
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }
        io.minka.api.model.SignerResponse waddress = sdkApiClient.deleteSigner("waddress");
    }

    @Test
    public void shouldGetSignersWithPaging() throws io.minka.api.handler.ApiException {

        SignerListResponse signers = sdkApiClient.getSigners(1, 2);
        System.out.println(signers.size());
        assertEquals(signers.size(), 3);//BECAUSE OF THE pagesize + 1 error element

        io.minka.api.model.SignerResponse wAddress = sdkApiClient.getSignerByAddress("wAddress");
        SignerRequest req = new SignerRequest();
        Map<String, Object> labels = new HashMap<>();
        labels.put("description", "saving");
        req.setLabels(labels);
        sdkApiClient.updateSigner("wAddress", req);
    }

    @Test
    public void createSignerForOnlineUse(){

        Map<String, Object> labels = new HashMap<>();


        try {
            SignerResponse signer = sdkApiClient.createSigner(labels);
            System.out.println(signer);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
        }
    }


}
