package com.minka;

import com.minka.api.handler.ApiException;
import com.minka.api.model.SignerResponse;
import com.minka.wallet.primitives.utils.SdkApiClient;

import java.util.HashMap;
import java.util.Map;

public class createUserBridgeAccount {

    public static void main(String args[]) {

        SdkApiClient sdkApiClient = new SdkApiClient("https://achtin-tst.minka.io/v1", "5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c");

        Map<String, Object> labels = new HashMap<>();
        labels.put("description", "bridge");
        labels.put("bank_account_number", "USER_BRIDGE_ACCOUNT_NUMBER");
        labels.put("bifci", "YOUR_BANK_BICFI_CODE");

        try {
            SignerResponse signer = sdkApiClient.createSigner(labels);
            System.out.println(signer);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
        }
    }
}
