package com.minka;

import com.minka.api.handler.ApiException;
import com.minka.api.model.WalletResponse;
import com.minka.wallet.primitives.utils.SdkApiClient;

import java.util.HashMap;
import java.util.Map;


public class main {

    public static void main(String args[]){

        SdkApiClient sdkApiClient = new SdkApiClient("https://achtin-tst.minka.io/v1","5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c");

        String yourBankName = "$yourbankname2";

        Map<String, Object> labels = new HashMap<>();
        labels.put("name", "YOUR BANK NAME");
        labels.put("bicfi", "YOUR_BANK_BICFI_CODE");

        WalletResponse wallet = null;
        try {
            wallet = sdkApiClient.createWallet(yourBankName, labels);
            System.out.println(wallet);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
        }

    }
}