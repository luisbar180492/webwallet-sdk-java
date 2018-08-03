package com.minka;

import com.minka.api.handler.ApiException;
import com.minka.api.model.BalanceResponse;
import com.minka.api.model.Keeper;
import com.minka.wallet.primitives.utils.SdkApiClient;

public class getKeeper {

    public static void main(String args[]) throws ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient("https://achtin-tst.minka.io/v1","5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c");

        try {

            Keeper keeper = sdkApiClient.getKeeper();
            System.out.println(keeper);

        } catch (ApiException e) {
            System.out.println(e.getCode());

            System.out.println(e.getResponseBody());
        }
    }
}
