package com.minka.sdk;

import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.AcceptTransferRequest;
import io.minka.api.model.CreateTransferRequest;
import io.minka.api.model.CreateTransferResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TinTranferTesting {

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

    @Test
    public void createTransferEnvio() throws ApiException {

        CreateTransferRequest tinTranfer = new CreateTransferRequest();
        tinTranfer.setSource("wM9kx8Bzsp51TRefSccAhH57KU4a2rfS5y");
        tinTranfer.setTarget("$573004431529");
        Map<String, Object> labels = new HashMap<>();
        labels.put("type", "SEND");
        labels.put("description", "Description of a transfer");
        tinTranfer.setLabels(labels);
        tinTranfer.setAmount("1");
        tinTranfer.setSymbol("$tin");

        tinTranfer.setLabels(labels);
        CreateTransferResponse tinTransfer = sdkApiClient.createTinTransfer(tinTranfer);

        System.out.println(tinTransfer);

    }

    @Test
    public void acceptTransfer() throws ApiException {

//        String actionRequestId = "f146e76a-8566-4691-9d90-2209a856ca5c";
//        String action;
//        action = sdkApiClient.rejectTransfer(req, actionRequestId);
//        System.out.println("ACTION ID " + action);

    }


    @Test
    public void rejectTransfer() throws ApiException {

//        String handleTargetAddress = "wic6kF3M2Zvy986mByV76YKXDFNY1spa6f";
//        String actionRequestId = "f146e76a-8566-4691-9d90-2209a856ca5c";
//        String action;
//         sdkApiClient.rejectTransferRequest();
//        System.out.println("ACTION ID " + action);

    }

}
