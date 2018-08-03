package com.minka;

import com.minka.api.handler.ApiException;
import com.minka.api.model.CreateActionRequest;
import com.minka.api.model.CreateActionResponse;
import com.minka.api.model.GenericResponse;
import com.minka.api.model.SignerResponse;
import com.minka.wallet.primitives.utils.SdkApiClient;

import java.util.HashMap;
import java.util.Map;

public class chargeMoneyToAnAccount {

    public static void main(String args[]) throws ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient("https://achtin-tst.minka.io/v1","5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c");

        try {
            CreateActionRequest req = new CreateActionRequest();
            Map<String, Object> labels = new HashMap<>();
            labels.put("type", "UPLOAD");
            labels.put("cus", "CUS");

            req.setLabels(labels);
            req.setAmount("1");
            req.setSource("$tin");
            req.setSymbol("$tin");
            req.setTarget("$userphonenumber1");
            System.out.println(req);

            CreateActionResponse action = sdkApiClient.createAction(req);
            System.out.println(action);

            Object hash = action.get("hash");
            String hashValue = hash.toString();
            System.out.println("hashValue:" + hashValue);
            System.out.println("hashValue:");

            GenericResponse actionFound = sdkApiClient.getAction(hashValue);
            System.out.println("ActionRetrieved:");
            System.out.println(actionFound);
        } catch (ApiException e) {
            e.printStackTrace();
            System.out.println(e.getResponseBody());
        }
    }
}
