package com.minka.wallet;

import com.minka.api.handler.ApiException;
import com.minka.api.model.BalanceResponse;
import com.minka.wallet.primitives.utils.SdkApiClient;

public class getBalance {

    public static void main(String args[]) throws ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient("https://achtin-tst.minka.io/v1","5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c");

        try {
            String bankName = "$yourbankname1221";
            String currency = "$tin";
            BalanceResponse balance = sdkApiClient.getBalance(bankName, currency);
            System.out.println(balance);

        } catch (ApiException e) {
            System.out.println(e.getCode());

            System.out.println(e.getResponseBody());
        }
    }
}
