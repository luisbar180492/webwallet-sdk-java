package com.minka;


import com.minka.api.handler.ApiException;
import com.minka.api.model.WalletUpdateResponse;
import com.minka.wallet.primitives.utils.SdkApiClient;

import java.util.ArrayList;
import java.util.List;

public class Exercise8Step1 {

    public static void main(String args[]) throws ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient("https://achtin-tst.minka.io/v1","5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c");

        try {
            String handle = "$userphonenumber";
            String defaultAddress = "USER_BRIDGE_ACCOUNT_ADDRESS";
            List<String> signers = new ArrayList<>();
            signers.add(defaultAddress);
            WalletUpdateResponse signer = sdkApiClient.updateWallet(handle, signers, defaultAddress);
            System.out.println(signer);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
        }
    }
}