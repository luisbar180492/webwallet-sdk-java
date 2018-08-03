package com.minka;

import com.minka.api.handler.ApiException;
import com.minka.api.model.CreateActionRequest;
import com.minka.api.model.CreateActionResponse;
import com.minka.api.model.SignerResponse;
import com.minka.wallet.primitives.utils.SdkApiClient;

import java.util.HashMap;
import java.util.Map;

public class chargeMoneyToAnAccount {

    public static void main(String args[]) throws ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient("https://achtin-tst.minka.io/v1","5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c");

        try {
            CreateActionRequest req = new CreateActionRequest();
            req.setLabels(new HashMap<>());
            req.setAmount("100");
            req.setSource("$tin");
            req.setSymbol("$tin");
            req.setTarget("$yourbankname1221");
            CreateActionResponse action = sdkApiClient.createAction(req);
            System.out.println(action);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
        }
    }
}
