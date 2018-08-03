package com.minka;


import com.minka.api.handler.ApiException;
import com.minka.api.model.WalletResponse;
import com.minka.wallet.primitives.utils.SdkApiClient;

import java.util.HashMap;
import java.util.Map;

public class createPersonWallet {

    public static void main(String args[]){

        SdkApiClient sdkApiClient = new SdkApiClient("https://achtin-tst.minka.io/v1","5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c");

        String handle = "$userphonenumber";

        Map<String, Object> labels = new HashMap<>();
        labels.put("first_name", "user_first_name");
        labels.put("last_name", "user_last_name");
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("sms", "user_phone_number");
        labels.put("channel", smsMap);
        WalletResponse wallet = null;
        try {
            wallet = sdkApiClient.createWallet(handle, labels);
            System.out.println(wallet);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
        }

    }
}
