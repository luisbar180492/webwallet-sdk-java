package com.minka.sdk;

import com.minka.ExceptionResponseTinApi;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TinTranferTesting {

    SdkApiClient sdkApiClient;

    @Before
    public void prepare() throws ApiException {

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_TESTING,
                TestingConstants.API_KEY, TestingConstants.TESTING_BASE);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        //sdkApiClient.setTimeout(20);
       // this.sdkApiClient.setProxy(TestingConstants.PROXY_HOST, TestingConstants.PROXY_PORT);

    }

    @Ignore
    @Test
    public void createTransferEnvio() {
        String userEnvioAddress= "wQxWXHPCDcfmGnuNPRAxfVoxWH79YcGBJV";

        CreateTransferRequest tinTranfer = new CreateTransferRequest();
        tinTranfer.setSource(userEnvioAddress);
        tinTranfer.setTarget("$joeluser13");
        CreateTransferRequestLabels labels = new CreateTransferRequestLabels();
        labels.setTxId("TxId");
        labels.setType("SEND");
        labels.setDescription("Description of a transfer");
        tinTranfer.setLabels(labels);
        tinTranfer.setAmount("1");
        tinTranfer.setSymbol("$tin");

        tinTranfer.setLabels(labels);
        CreateTransferResponse tinTransfer =
                null;
        try {
            tinTransfer = sdkApiClient.createTinTransfer(tinTranfer);
        } catch (ApiException e) {
            System.out.println(e.getCode());
            System.out.println(e.getResponseBody());
        }

        System.out.println(tinTransfer);
    }

    @Ignore
    @Test
    public void acceptTransfer()  {

        String actionRequestId = "005b6da8-86d3-4647-b569-b87c1ed199b5";

        AcceptTransferRequest req =new AcceptTransferRequest();
        WalletObject wallet = new WalletObject();
        wallet.setHandle("$573016545645");
        req.setWallet(wallet);
        SignerObject signer = new SignerObject();
        signer.setHandle("wewAHE6F5HjWYr5io2HPwtgFqx8jmUcHfW");
        req.setSigner(signer);
        CreateTransferResponse createTransferResponse =
                null;
        try {
            createTransferResponse = sdkApiClient.acceptTransfer(req, actionRequestId);
        } catch (ExceptionResponseTinApi e) {
            System.out.println(e.getErrorCode());
            System.out.println(e.getMessage());
        }

        System.out.println(createTransferResponse);
    }


    @Test
    public void rejectTransfer() throws ApiException {

//        String handleTargetAddress = "wic6kF3M2Zvy986mByV76YKXDFNY1spa6f";
//        String actionRequestId = "f146e76a-8566-4691-9d90-2209a856ca5c";
//        String action;
//         sdkApiClient.rejectTransferRequest();
//        System.out.println("ACTION ID " + action);

    }

}
