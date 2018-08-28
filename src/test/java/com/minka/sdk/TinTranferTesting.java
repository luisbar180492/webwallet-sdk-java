package com.minka.sdk;

import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.CreateTransferRequest;
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
        tinTranfer.setSource("wY2gpPNzJy6eudaBXMPpXooyuk523WLhC7");
        tinTranfer.setTarget("$perro_uno");
        Map<String, Object> labels = new HashMap<>();
        labels.put("type", "SEND");
        labels.put("description", "Description of a transfer");
        tinTranfer.setLabels(labels);
        tinTranfer.setAmount("1");
        tinTranfer.setSymbol("$tin");

        tinTranfer.setLabels(labels);
        sdkApiClient.createTinTransfer(tinTranfer);

    }
}
