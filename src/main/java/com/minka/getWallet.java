package com.minka;


import com.minka.api.handler.ApiException;
import com.minka.api.model.GetWalletResponse;
import com.minka.wallet.primitives.utils.SdkApiClient;

public class getWallet {

    public static void main(String args[]){

        SdkApiClient sdkApiClient = new SdkApiClient("https://achtin-tst.minka.io/v1","5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c");

        String yourBankName = "$yourbankname1221";

        try {
            GetWalletResponse wallet = sdkApiClient.getWallet(yourBankName);
            System.out.println(wallet);
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }

    }
}
